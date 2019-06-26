#!/bin/bash

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
source $SCRIPT_DIR/base.sh

CURRENT_DIR=$(pwd)

deploy_dir=$CURRENT_DIR/build/deploy
if [ ! -d $deploy_dir ];then
  mkdir $deploy_dir
fi

echo "--- Configuring tools..."
gcloud config set project $PROJECT
gcloud config set compute/zone us-east1-b
gcloud auth configure-docker
gcloud container clusters get-credentials photos-$ENVIRONMENT

echo "--- Building frontend ---"
pushd photos-frontend > /dev/null
npm install
npm run bundle
popd > /dev/null

echo "--- Building projects ---"
./gradlew clean build

for project in photos-frontend photos-service; do
  pushd $project > /dev/null

  SOURCE_IMAGE=$project:$GIT_SHA

  echo "--- Building Docker image: $SOURCE_IMAGE"
  docker build --build-arg JAR_FILE=build/libs/$project-$VERSION.jar --tag $SOURCE_IMAGE .

  echo "--- Publishing Docker images: $SOURCE_IMAGE"
  DEST_IMAGE=gcr.io/$PROJECT/$SOURCE_IMAGE
  docker tag $SOURCE_IMAGE $DEST_IMAGE
  docker push $DEST_IMAGE

  echo "--- Generating K8s file for: $project"
  
  CONFIGURATION_FILE="application-$ENVIRONMENT.yml"
  if [ -f $CONFIGURATION_FILE ]; then
    echo "Configuration file found for $project and environment $ENVIRONMENT. Deploying secret..."
    cp $CONFIGURATION_FILE $deploy_dir/application.yml
    kubectl delete secret --ignore-not-found $project
    kubectl create secret generic $project --from-file $deploy_dir/application.yml
    rm $deploy_dir/application.yml
  fi

  for f in src/main/kubernetes/*.yml; do
    filename=$(basename $f)
    echo "Processing '$f'"

    cat $f | sed "s/GIT_SHA/$GIT_SHA/g" | \
      sed "s/VERSION/$VERSION/g" | \
      sed "s!DOCKER_IMAGE!$DEST_IMAGE!g" | \
      sed "s/ENVIRONMENT/$ENVIRONMENT/g" > $deploy_dir/$project-$filename
  done
  popd > /dev/null
done

echo "--- Applying files to cluster..."
kubectl apply -f $deploy_dir
