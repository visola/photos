provider "google" {
    credentials = "${file("credentials.json")}"
    project     = var.project_id
    region      = var.region
    zone        = var.zone
}

resource "google_compute_instance" "base_instance" {
    name = "life-booster-${var.environment}"
    machine_type = "g1-small"

    boot_disk {
        initialize_params {
            image = "gce-uefi-images/ubuntu-1804-lts"
        }
    }

    labels = {
        environment = var.environment
    }

    metadata_startup_script = "sudo apt-get update; sudo apt-get install -y openjdk-8-jre;"

    network_interface {
        access_config {
            network_tier = "PREMIUM"
        }
        network = "default"
    }
}

resource "google_storage_bucket" "image-store" {
    name     = "life-booster-${var.environment}"
    location = var.region
    force_destroy = true

    labels = {
        environment = var.environment
    }

    storage_class = "REGIONAL"
}

resource "google_storage_bucket" "thumbnail-store" {
    name     = "life-booster-${var.environment}-thumbnails"
    location = var.region
    force_destroy = true

    labels = {
        environment = var.environment
    }

    storage_class = "REGIONAL"
}