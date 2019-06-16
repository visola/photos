#!/bin/bash

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
CURRENT_DIR=$(pwd)

source $SCRIPT_DIR/base.sh

echo "--- Configuring tools..."
gcloud config set project $PROJECT
gcloud config set compute/zone us-east1-b
gcloud container clusters get-credentials photos-$ENVIRONMENT

if [[ -z "$3" || -z "$4" ]]; then
  echo "3rd and 4th arguments need to be cert and private key file."
  exit -1
fi

CERTIFICATE_DATA=$(cat $3 | base64)
PRIVATE_KEY_DATA=$(cat $4 | base64)

cat <<EOM >tls_secret.yml
apiVersion: v1
kind: Secret
metadata:
  name: photos-$ENVIRONMENT.vinnieapps.com
  namespace: default
type: kubernetes.io/tls
data:
  tls.crt: $CERTIFICATE_DATA
  tls.key: $PRIVATE_KEY_DATA
EOM

kubectl apply -f tls_secret.yml

rm tls_secret.yml

