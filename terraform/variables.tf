variable "project_name" {
  default = "snowman"
}

# Fixed: East US had no MySQL Flexible Server capacity for this subscription,
# and later region attempts hit AKS vCPU quota limits. South India is where
# the working deployment (snowman-rg) ended up living - see resource group
# screenshot with snowman-aks, snowman-mysql, etc. all successfully created here.
variable "location" {
  default = "South India"
}

variable "resource_group_name" {
  default = "snowman-rg"
}

variable "vnet_address_space" {
  default = ["10.0.0.0/16"]
}

variable "aks_subnet_prefix" {
  default = ["10.0.1.0/24"]
}

variable "appgw_subnet_prefix" {
  default = ["10.0.2.0/24"]
}

variable "aks_node_count" {
  default = 2
}

# Fixed: Standard_DS2_v2 was not available in the target region/subscription.
# Standard_B2s is widely available and fine for a dev/test AKS node pool.
variable "aks_vm_size" {
  default = "Standard_B2s_v2"
}

variable "mysql_admin_username" {
  default = "snowmanadmin"
}

# Fixed: "password" failed Azure's complexity rule (needs 3 of 4: upper,
# lower, number, special char). Set a real one via:
#   export TF_VAR_mysql_admin_password='Snowman@2026Pass'
# Do NOT hardcode a default here - keep it env-var only.
variable "mysql_admin_password" {
  description = "Set via TF_VAR_mysql_admin_password env var - do not hardcode"
  sensitive   = true
}

variable "acr_name" {
  description = "Must be globally unique, alphanumeric only. Override with -var if 'snowmanacr' is taken."
  default     = "snowmanacr"
}
