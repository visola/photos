#!/bin/bash

set -e

function usage() {
    echo "Usage:"
    echo "   ./deploy_functions.sh {project_id} {environment}"
    echo
    echo "  project_id      Google Cloud Project to deploy the function to."
    echo "  environment     Name of the environment that will be created."
}

if [ -z "$1" ]; then
    usage
    exit 1
fi

if [ -z "$2" ]; then
    usage
    exit 1
fi

PROJECT=$1
ENVIRONMENT=$2
REGION=us-east1
VERSION=$(./gradlew properties | grep version | awk '{ print $2 }')

IMAGE_BUCKET_NAME=life-booster-$ENVIRONMENT

gcloud config set project $PROJECT
gcloud config set compute/zone us-east1-b

INSTANCE_NAME="life-booster-$(date +%Y%m%d%H%M%S)"

echo "Creating Cloud Compute instance $INSTANCE_NAME..."
gcloud compute instances create $INSTANCE_NAME \
  --machine-type g1-small \
  --labels environment=$ENVIRONMENT \
  --image-family ubuntu-1804-lts --image-project gce-uefi-images

echo "Installing OpenJDK JRE..."
gcloud compute ssh $INSTANCE_NAME --command='sudo apt-get update && sudo apt-get install -y openjdk-8-jre'

echo "Creating storage buckets..."
gsutil mb -b on -c regional -l $REGION gs://$IMAGE_BUCKET_NAME
gsutil mb -b on -c regional -l $REGION gs://$IMAGE_BUCKET_NAME-thumbnails
