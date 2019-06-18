#!/bin/bash

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
CURRENT_DIR=$(pwd)

source $SCRIPT_DIR/base.sh

echo "--- Building package..."
rm -Rf build
npm install
./gradlew clean build distZip

echo "--- Building Docker image..."
SOURCE_IMAGE=photos:$GIT_SHA

pushd build/distributions > /dev/null
unzip photos-${VERSION}.zip
docker build --file $CURRENT_DIR/Dockerfile --build-arg "FILES_DIR=photos-${VERSION}/" --tag $SOURCE_IMAGE .
popd > /dev/null

echo "--- Configuring tools..."
gcloud config set project $PROJECT
gcloud config set compute/zone us-east1-b
gcloud auth configure-docker
gcloud container clusters get-credentials photos-$ENVIRONMENT

echo "--- Publishing Docker Images..."
DEST_IMAGE=gcr.io/$PROJECT/$SOURCE_IMAGE
docker tag $SOURCE_IMAGE $DEST_IMAGE
docker push $DEST_IMAGE

echo "--- Generating files for kubernetes..."
deploy_dir=build/deploy
if [ ! -d $deploy_dir ];then
  mkdir $deploy_dir
fi

CONFIGURATION_FILE="application-$ENVIRONMENT.yml"
if [ -f $CONFIGURATION_FILE ]; then
  cp $CONFIGURATION_FILE $deploy_dir/application.yml
  kubectl delete secret --ignore-not-found photos-service
  kubectl create secret generic photos-service --from-file $deploy_dir/application.yml
  rm $deploy_dir/application.yml
fi


for f in $INFRA_DIR/kubernetes/*.yml; do
  filename=$(basename $f)
  echo "Processing '$filename'"

  cat $f | sed "s/GIT_SHA/$GIT_SHA/g" | \
    sed "s/VERSION/$VERSION/g" | \
    sed "s!DOCKER_IMAGE!$DEST_IMAGE!g" | \
    sed "s/ENVIRONMENT/$ENVIRONMENT/g" > $deploy_dir/$filename
done

echo "--- Applying to cluster..."
kubectl apply -f $deploy_dir
