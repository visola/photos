data "template_file" "init_script" {
  template = file("${path.module}/init_script_template.sh")
  vars = {
    db_name     = var.db_name
    db_password = var.db_password
  }
}

resource "google_compute_instance" "mysql_instance" {
  name = "${var.db_name}-instance"
  machine_type = "g1-small"

  boot_disk {
    initialize_params {
      image = "gce-uefi-images/ubuntu-1804-lts"
    }
  }

  tags = [ "database", "mysql" ]

  labels = {
    environment = var.environment
  }

  metadata_startup_script = data.template_file.init_script.rendered

  network_interface {
    network = "default"
  }
}