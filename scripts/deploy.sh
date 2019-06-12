#!/bin/bash

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
source $SCRIPT_DIR/base.sh

PROJECT=$1
ENVIRONMENT=$2
REGION=us-east1
DEPLOY_VERSION=$VERSION-$GIT_SHA
CONFIGURATION_FILE="application-$ENVIRONMENT.yml"

IMAGE_BUCKET_NAME=photos-$ENVIRONMENT

gcloud config set project $PROJECT
gcloud config set compute/zone us-east1-b

echo "Building..."
./gradlew clean build > /dev/null

INSTANCE_NAME=$(gcloud compute instances list --filter labels.environment=$ENVIRONMENT --format json | jq -r '.[] | .name')
JAR_NAME=photos-${VERSION}.jar
DEPLOY_JAR=photos-$DEPLOY_VERSION.jar

echo "Shutting down old version..."
gcloud compute scp src/main/scripts/shutdown.sh $INSTANCE_NAME:shutdown.sh
gcloud compute ssh $INSTANCE_NAME --command="./shutdown.sh"

echo "Copying files..."
gcloud compute scp src/main/scripts/run.sh $INSTANCE_NAME:run.sh
gcloud compute scp build/libs/$JAR_NAME $INSTANCE_NAME:$DEPLOY_JAR

if [ -f "$CONFIGURATION_FILE" ]; then
    gcloud compute scp $CONFIGURATION_FILE $INSTANCE_NAME:application.yml
fi

gcloud compute ssh $INSTANCE_NAME --command="ls -la"

echo "Deploying to ${INSTANCE_NAME}..."
gcloud compute ssh $INSTANCE_NAME --command="./run.sh $DEPLOY_JAR"
