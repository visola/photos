#!/bin/bash

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
source $SCRIPT_DIR/base_terraform.sh

PROJECT=$1
ENVIRONMENT=$2

pushd ./terraform > /dev/null
terraform destroy -var "project_id=$PROJECT" -var "environment=$ENVIRONMENT"
popd ./terraform > /dev/null
