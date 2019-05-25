#!/bin/bash

set -e

function usage() {
    echo "Usage:"
    echo "   ./deploy_functions.sh {project_id} {upload_bucket}"
    echo
    echo "  project_id        Google Cloud Project to deploy the function to."
    echo "  upload_bucket     Bucket where images will be uploaded to."
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
IMAGE_BUCKET_NAME=$2
REGION=us-east1

gsutil mb -c regional -l $REGION gs://$IMAGE_BUCKET_NAME
gsutil mb -c regional -l $REGION gs://$IMAGE_BUCKET_NAME-thumbnails

gcloud functions deploy generateThumb \
    --region $REGION \
    --project $PROJECT \
    --runtime nodejs8 \
    --trigger-bucket $2 \
    --set-env-vars "THUMBNAIL_BUCKET=$IMAGE_BUCKET_NAME-thumbnails" \
    --source src/main/functions/thumb_generator
