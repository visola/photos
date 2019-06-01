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

    tags = [ "web" ]

    labels = {
        environment = var.environment
    }

    metadata_startup_script = <<INIT_SCRIPT
        sudo apt-get update
        sudo apt-get install -y openjdk-8-jre

        sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password password ${var.db_password}'
        sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password ${var.db_password}'
        sudo apt-get -y install mysql-server
        mysql -u root '-p${var.db_password}' -e 'CREATE DATABASE life_booster'
    INIT_SCRIPT

    network_interface {
        access_config {
            network_tier = "PREMIUM"
        }
        network = "default"
    }
}

resource "google_storage_bucket" "image_store" {
    name     = "life-booster-${var.environment}"
    location = var.region
    force_destroy = true

    labels = {
        environment = var.environment
    }

    storage_class = "REGIONAL"
}

resource "google_storage_bucket" "thumbnail_store" {
    name     = "life-booster-${var.environment}-thumbnails"
    location = var.region
    force_destroy = true

    labels = {
        environment = var.environment
    }

    storage_class = "REGIONAL"
}

resource "google_storage_bucket" "functions_source" {
  name = "functions_source_${var.environment}"
}

data "archive_file" "thumbnail_generator" {
  type        = "zip"
  source_dir  = "${var.root_dir}/src/main/functions/thumbnail_generator"
  output_path = "${var.build_dir}/thumbnail_generator.zip"
}

resource "google_storage_bucket_object" "generate_thumbnail_source" {
  name   = "generate_thumbnail_source.zip"
  bucket = "${google_storage_bucket.functions_source.name}"
  source = data.archive_file.thumbnail_generator.output_path
}

resource "google_cloudfunctions_function" "generate_thumbnail" {
    name                  = "generate_thumbnail_${var.environment}"
    description           = "Function to generate thumbnails from uploaded photos."
    runtime               = "nodejs10"
    entry_point           = "generate_thumbnail_${var.environment}"

    available_memory_mb   = 256

    source_archive_bucket = google_storage_bucket.functions_source.name
    source_archive_object = google_storage_bucket_object.generate_thumbnail_source.name

    environment_variables = {
        FUNCTION_NAME    = "generate_thumbnail_${var.environment}"
        THUMBNAIL_BUCKET = "${google_storage_bucket.thumbnail_store.name}"
    }

    event_trigger {
        event_type = "google.storage.object.finalize"
        resource   = google_storage_bucket.image_store.name
    }

    labels = {
        environment = var.environment
    }
}

data "google_compute_network" "default" {
  name = "default-us-east1"
}

resource "google_compute_firewall" "web_traffic" {
    name    = "web-traffic"
    network = data.google_compute_network.default.name

    allow {
        protocol = "tcp"
        ports    = ["80", "443", "8080"]
    }

    target_tags = [ "web" ]
}
