#!/bin/bash
set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

source $SCRIPT_DIR/base.sh

IMAGE_NAME=gcr.io/life-booster-201814/$BASE_NAME:$VERSION

docker tag $BASE_NAME:$VERSION $IMAGE_NAME
docker push $IMAGE_NAME
