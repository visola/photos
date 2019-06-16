provider "google" {
  credentials = file("credentials.json")
  project     = var.project_id
  region      = var.region
  zone        = var.zone
}

module "buckets" {
  source = "../modules/buckets"

  environment = var.environment
  region      = var.region
  zone        = var.zone
}
