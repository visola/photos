provider "google" {
  credentials = file("credentials.json")
  project     = var.project_id
  region      = var.region
  zone        = var.zone
}

data "google_client_config" "current" {
  // Empty block
}

provider "kubernetes" {
  host                   = module.primary_gke.endpoint

  cluster_ca_certificate = base64decode(module.primary_gke.cluster_ca_certificate)
  token                  = data.google_client_config.current.access_token
}

module "buckets" {
  source = "../modules/buckets"

  environment = var.environment
  region      = var.region
  zone        = var.zone
}

module "primary_gke" {
  source = "../modules/gke_cluster"

  environment = var.environment
  zone        = var.zone
}

module "mysql_database" {
  source = "../modules/mysql_instance"

  db_name     = var.db_name
  db_password = var.db_password
  environment = var.environment
}
