resource "google_container_cluster" "primary" {
  name     = "photos-${var.environment}"
  location = var.zone

  remove_default_node_pool = true
  initial_node_count = 1

  master_auth {
    username = ""
    password = ""

    client_certificate_config {
      issue_client_certificate = false
    }
  }
}

resource "google_container_node_pool" "primary_node_pool" {
  name       = "photos-${var.environment}-primary"
  location   = var.zone
  cluster    = "${google_container_cluster.primary.name}"
  node_count = var.node_code

  node_config {
    preemptible  = true
    machine_type = var.machine_type

    oauth_scopes = [
      "https://www.googleapis.com/auth/logging.write",
      "https://www.googleapis.com/auth/monitoring",
    ]
  }
}
