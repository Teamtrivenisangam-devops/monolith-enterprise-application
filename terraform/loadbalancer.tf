# NOTE ON THE LOAD BALANCER (L4) IN THE DIAGRAM:
#
# In AKS, the "Load Balancer" in front of the pods is normally created
# AUTOMATICALLY by Azure when you deploy a Kubernetes Service of
# type: LoadBalancer (see k8s/service.yaml in this project).
#
# We deliberately do NOT define a standalone azurerm_lb resource here,
# because creating one manually and also letting AKS create one via the
# Service manifest would result in two competing/duplicate load balancers.
#
# Traffic flow implemented in this setup:
#   Application Gateway (WAF, public-facing, defined in appgateway.tf)
#     -> backend_address_pool points at the AKS Service's internal Load Balancer IP
#     -> Kubernetes Service (type: LoadBalancer) load-balances across pods
#
# After `terraform apply` + `kubectl apply -f k8s/service.yaml`, get the
# Service's external/internal IP with:
#   kubectl get svc enterprise-application-service -n snowman
#
# Then update the Application Gateway backend pool to point to that IP
# (either manually in the Azure Portal, or via azurerm_application_gateway
# backend_address_pool.fqdns/ip_addresses once the IP is known - a good
# candidate for a follow-up Terraform data source once the cluster exists).
