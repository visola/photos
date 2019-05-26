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
DEPLOY_VERSION=$VERSION-$(date +%Y%m%d%H%M%S)

IMAGE_BUCKET_NAME=life-booster-$ENVIRONMENT

gcloud config set project $PROJECT
gcloud config set compute/zone us-east1-b

echo "Building..."
./gradlew build > /dev/null

echo "Deploying thumbnail generator function..."
gcloud functions deploy generateThumb \
    --region $REGION \
    --project $PROJECT \
    --runtime nodejs8 \
    --trigger-bucket $IMAGE_BUCKET_NAME \
    --set-env-vars "THUMBNAIL_BUCKET=$IMAGE_BUCKET_NAME-thumbnails" \
    --source src/main/functions/thumb_generator


INSTANCE_NAME=$(gcloud compute instances list --filter labels.environment=$ENVIRONMENT --format json | jq -r '.[] | .name')
JAR_NAME=life-booster-${VERSION}.jar
DEPLOY_JAR=life-booster-$DEPLOY_VERSION.jar
echo "Deploying to ${INSTANCE_NAME}..."

gcloud compute ssh $INSTANCE_NAME --command="ls -la"
gcloud compute scp src/main/scripts/run.sh $INSTANCE_NAME:run.sh
gcloud compute scp build/libs/$JAR_NAME $INSTANCE_NAME:$DEPLOY_JAR
gcloud compute ssh $INSTANCE_NAME --command="./run.sh $DEPLOY_JAR"
