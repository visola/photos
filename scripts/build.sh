#!/bin/bash

build_docker_image() {
  project_name=$1
  docker_image=$project_name:$VERSION

  echo "Building Docker image: $docker_image"
  pushd $project_name >> /dev/null
  docker build -t "$docker_image" --build-arg JAR_FILE=build/libs/$project_name-$VERSION.jar .
  popd >> /dev/null
}

main() {
  GIT_SHA=$(git rev-parse --short HEAD)
  VERSION=$(cat .version)

  echo "Git SHA: '$GIT_SHA'"
  echo "Version: '$VERSION'"

  echo "Building..."
  ./gradlew build

  build_docker_image 'photos-jobs'
  build_docker_image 'photos-service'
}

main
