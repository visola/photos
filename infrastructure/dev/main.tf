provider "google" {
  credentials = file("credentials.json")
  project     = var.project_id
  region      = var.region
  zone        = var.zone
}

module "mysql_database" {
  source = "../modules/mysql_instance"

  db_name     = var.db_name
  db_password = var.db_password
  environment = var.environment
}

module "buckets" {
  source = "../modules/buckets"

  environment = var.environment
  region      = var.region
  zone        = var.zone
}
