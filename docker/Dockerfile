FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=build /app/target/Snowman.jar /app/Snowman.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/Snowman.jar"]
................
