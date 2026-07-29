terraform {
  required_version = ">= 1.5.0"

  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.90"
    }
  }

  # Recommended: store state remotely (uncomment and configure once you have
  # a storage account created for state, e.g. via `az storage account create`)
  # backend "azurerm" {
  #   resource_group_name  = "tfstate-rg"
  #   storage_account_name = "tfstatesnowman"
  #   container_name       = "tfstate"
  #   key                  = "snowman.terraform.tfstate"
  # }
}

provider "azurerm" {
  features {}
}
