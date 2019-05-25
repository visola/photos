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

gcloud functions deploy generateThumb \
    --project $1 \
    --runtime nodejs8 \
    --trigger-bucket $2 \
    --set-env-vars "THUMBNAIL_BUCKET=$2-thumbnails" \
    --source src/main/functions/thumb_generator
