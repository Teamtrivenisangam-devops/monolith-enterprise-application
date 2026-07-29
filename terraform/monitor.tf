resource "azurerm_log_analytics_workspace" "main" {
  name                = "${var.project_name}-law"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name
  sku                 = "PerGB2018"
  retention_in_days   = 30
}

# Enable AKS Container Insights (metrics + logs from pods/nodes)
resource "azurerm_monitor_diagnostic_setting" "aks" {
  name                       = "${var.project_name}-aks-diagnostics"
  target_resource_id         = azurerm_kubernetes_cluster.main.id
  log_analytics_workspace_id = azurerm_log_analytics_workspace.main.id

  enabled_log {
    category = "kube-apiserver"
  }

  enabled_log {
    category = "kube-controller-manager"
  }

  metric {
    category = "AllMetrics"
  }
}

# Basic alert: notify if AKS node CPU usage is too high
resource "azurerm_monitor_action_group" "main" {
  name                = "${var.project_name}-alerts"
  resource_group_name = azurerm_resource_group.main.name
  short_name          = "snowman"

  email_receiver {
    name          = "devops-team"
    email_address = "devops@example.com"
  }
}
