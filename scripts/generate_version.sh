#!/bin/bash

scripts/semantic-release --token $GITHUB_TOKEN -dry -vf -slug VinnieApps/photos

exit 0 # never fail
