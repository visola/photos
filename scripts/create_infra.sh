#!/bin/bash

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
source $SCRIPT_DIR/base.sh

PROJECT=$1
ENVIRONMENT=$2

pushd ./terraform > /dev/null
terraform init
terraform apply -var "project_id=$PROJECT" -var "environment=$ENVIRONMENT"
popd ./terraform > /dev/null
