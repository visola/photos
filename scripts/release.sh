#!/bin/bash

install_gcp_tools() {
  # Download gcloud tools and set it up
  curl -o gcp-script.sh https://sdk.cloud.google.com
  chmod +x gcp-script.sh
  ./gcp-script.sh --install-dir=$(pwd) --disable-prompts

  # Setup credentials
  openssl aes-256-cbc -K $encrypted_bc40a34dabb2_key -iv $encrypted_bc40a34dabb2_iv -in google-key.json.enc -out google-key.json -d
  google-cloud-sdk/bin/gcloud auth activate-service-account --key-file=google-key.json

  # Login to Docker
  google-cloud-sdk/bin/gcloud auth configure-docker --quiet
}

publish_docker_image() {
  project_name=$1
  docker_image=$project_name:$VERSION
  publish_docker_image=gcr.io/$GCP_PROJECT_ID/$project_name:$VERSION

  echo "Publishing image for $project_name: $publish_docker_image"
  pushd $project_name >> /dev/null
  docker tag $docker_image $publish_docker_image
  docker push $publish_docker_image
  popd >> /dev/null
}

main() {
  GIT_SHA=$(git rev-parse --short HEAD)
  VERSION=$(cat .version)
  GCP_PROJECT_ID=$1

  echo "Git SHA: '$GIT_SHA'"
  echo "Version: '$VERSION'"
  echo "GCP Project: '$GCP_PROJECT_ID'"

  if [ "$TRAVIS_BRANCH" != "master" ]; then
    echo "Not in master branch, exiting..."
    exit 0
  fi

  install_gcp_tools

  publish_docker_image 'photos-jobs'
  publish_docker_image 'photos-service'

  scripts/semantic-release --token $GITHUB_TOKEN -slug VinnieApps/photos
}

usage() {
    echo "Usage:"
    echo "   ./release.sh {GCP_PROJECT_ID}"
    echo
    echo "  GCP_PROJECT_ID      Google Cloud Project to release to."
}

if [ -z "$1" ]; then
    usage
    exit 1
fi

main $1
