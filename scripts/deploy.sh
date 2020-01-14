#!/bin/bash

publish_docker_image() {
  project_name=$1
  docker_image=$project_name:latest
  publish_docker_image=gcr.io/$GCP_PROJECT_ID/$project_name/$GIT_SHA

  echo "Building and publishing image for $project_name: $publish_docker_image"
  pushd $project_name >> /dev/null
  docker build -t $docker_image --build-arg JAR_FILE=build/libs/$project_name-$VERSION.jar .
  docker tag $docker_image $publish_docker_image
  docker push $publish_docker_image
  popd >> /dev/null
}

main() {
  GIT_SHA=$(git rev-parse --short HEAD)
  VERSION=$(gradle properties -q | grep "^version:" | awk '{print $2}')
  GCP_PROJECT_ID=$1

  echo "Git SHA: '$GIT_SHA'"
  echo "Version: '$VERSION'"
  echo "GCP Project: '$GCP_PROJECT_ID'"

  echo "Building..."
  ./gradlew build

  publish_docker_image 'photos-jobs'
  publish_docker_image 'photos-service'

  # TODO - Deploy to kubernetes
}

usage() {
    echo "Usage:"
    echo "   ./deploy_functions.sh {GCP_PROJECT_ID}"
    echo
    echo "  GCP_PROJECT_ID      Google Cloud Project to deploy the function to."
}

if [ -z "$1" ]; then
    usage
    exit 1
fi

main $1
