#!/bin/bash
set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

source $SCRIPT_DIR/base.sh

kubectl set image deployment/life-booster life-booster=gcr.io/life-booster-201814/life-booster:$VERSION
