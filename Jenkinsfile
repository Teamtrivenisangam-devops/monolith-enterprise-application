pipeline {
    agent any
    environment {
        PROJECT_DIR      = '/home/azureuser/monolith-enterprise-application'
        GIT_REPO_URL      = 'https://github.com/Teamtrivenisangam-devops/monolith-enterprise-application.git'
        GIT_BRANCH        = 'main'
        ACR_NAME         = 'snowmanacr'
        ACR_LOGIN_SERVER = "${ACR_NAME}.azurecr.io"
        IMAGE_NAME       = 'enterprise-application'
        IMAGE_TAG        = "${env.BUILD_NUMBER}"
        FULL_IMAGE       = "${ACR_LOGIN_SERVER}/${IMAGE_NAME}:${IMAGE_TAG}"
        AKS_NAMESPACE    = 'snowman'
        AKS_DEPLOYMENT   = 'enterprise-application'
        AKS_CONTAINER    = 'enterprise-application'
        SONAR_PROJECT_KEY = 'enterprise-application'
    }
    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }
    stages {

        stage('Checkout') {
    steps {
        dir("${PROJECT_DIR}") {
            sh '''
                pwd
                ls -la
                git config --global --add safe.directory "$PWD"
                git fetch origin
                git checkout ${GIT_BRANCH}
                git pull origin ${GIT_BRANCH}
            '''
        }
    }
}

        stage('Maven Build') {
            steps {
                dir("${PROJECT_DIR}") {
                    sh 'mvn -B clean compile'
                }
            }
        }

        stage('Unit Test') {
            steps {
                dir("${PROJECT_DIR}") {
                    // PowerMock 1.7.3 is incompatible with JDK 17 - ignoring failures for now.
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit testResults: "${PROJECT_DIR}/target/surefire-reports/*.xml", allowEmptyResults: true
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir("${PROJECT_DIR}") {
                    withSonarQubeEnv('SonarQubeServer') {
                        sh """
                            mvn -B sonar:sonar \
                              -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                              -Dsonar.projectName=${SONAR_PROJECT_KEY}
                        """
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package JAR') {
            steps {
                dir("${PROJECT_DIR}") {
                    sh 'mvn -B package -DskipTests'
                    archiveArtifacts artifacts: 'target/Snowman.jar', fingerprint: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                dir("${PROJECT_DIR}") {
                    sh "docker build -t ${FULL_IMAGE} -f docker/Dockerfile ."
                }
            }
        }

        stage('Push to ACR') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'azure-service-principal',
                    usernameVariable: 'ACR_USER',
                    passwordVariable: 'ACR_PASS'
                )]) {
                    sh '''
                        echo "$ACR_PASS" | docker login "$ACR_LOGIN_SERVER" -u "$ACR_USER" --password-stdin
                        docker push "$FULL_IMAGE"
                        docker logout "$ACR_LOGIN_SERVER"
                    '''
                }
            }
        }

        stage('Deploy to AKS') {
            steps {
                withCredentials([file(credentialsId: 'aks-kubeconfig', variable: 'KUBECONFIG_FILE')]) {
                    sh '''
                        export KUBECONFIG="$KUBECONFIG_FILE"
                        kubectl set image deployment/${AKS_DEPLOYMENT} \
                          ${AKS_CONTAINER}=${FULL_IMAGE} \
                          -n ${AKS_NAMESPACE}
                        if kubectl rollout status deployment/${AKS_DEPLOYMENT} -n ${AKS_NAMESPACE} --timeout=300s; then
                            echo "Rollout succeeded"
                        else
                            echo "Rollout failed — rolling back deployment/${AKS_DEPLOYMENT}"
                            kubectl rollout undo deployment/${AKS_DEPLOYMENT} -n ${AKS_NAMESPACE}
                            kubectl rollout status deployment/${AKS_DEPLOYMENT} -n ${AKS_NAMESPACE} --timeout=180s
                            exit 1
                        fi
                    '''
                }
            }
        }
    }
    post {
        success {
            echo "Deployed ${FULL_IMAGE} to AKS namespace ${AKS_NAMESPACE}"
        }
        failure {
            echo "Pipeline failed — check stage logs above. If deployment was attempted, it may have been auto-rolled-back."
        }
    }
}
