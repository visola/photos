#!/bin/bash
set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

source $SCRIPT_DIR/base.sh

docker build -t life-booster:$VERSION .
