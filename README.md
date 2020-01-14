# photos

This project is a playground for me to learn and practice the technologies involved in a real world application with multiple modules deployed to Kubernetes in Google Cloud using Terraform to manage the infrastructure.

:baby: This is a work in progress and it's still a baby. :baby:

# Pre-requisites

This project was built and tested using the following stack:

- JDK 8 - Code is in Java and uses Gradle to build
- MySQL - It needs a MySQL database
- Google Cloud - It uses resources from Google Cloud like Storage and the Vision API
- Docker - It's final deliverable is a Docker image
- Kubernetes - The deployment scripts assume it will be deployed in a GKE Kubernetes environment

The repository [vinnieapps-infrastructure](https://github.com/VinnieApps/vinnieapps-infrastructure/) contains infrastructure as code that can be used to create different environments for this application, including what you need to run it locally.

The deploy script uses the following applications:

- `gcloud` - to communicate with Google Cloud
- `docker` - to build and publish Docker images
- `kubectl` - to deploy the Kubernetes resources to the GKE cluster

# Build and deploy

To deploy, run the `scripts/deploy.sh` script. It will build the docker images, publish them to GCP and invoke `kubectl` to update the deployments in the selected Kubernetes cluster.
