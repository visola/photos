locals {
  service_name = "photos-service"
  service_port = 8080
}

resource "kubernetes_secret" "tls_certificate" {
  metadata {
    name = substr(var.domain, 0, length(var.domain) - 1)
  }

  data = {
    "tls.crt" = var.tls_certificate
    "tls.key" = var.tls_private_key
  }

  type = "kubernetes.io/tls"
}

resource "kubernetes_service" "photos_service" {
  metadata {
    name = local.service_name
  }

  spec {
    type = "NodePort"

    selector = {
      app = "photos-service"
    }

    port {
      port        = local.service_port
      target_port = local.service_port
    }
  }
}

resource "kubernetes_ingress" "photos_ingress" {
  metadata {
    name = "photos-ingress"
  }

  spec {
    tls {
      secret_name = kubernetes_secret.tls_certificate.metadata[0].name
    }

    rule {
      http {
        path {
          path = "/"
          backend {
            service_name = local.service_name
            service_port = local.service_port
          }
        }
      }
    }
  }
}