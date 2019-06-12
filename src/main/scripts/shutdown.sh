#!/bin/bash

PID=$(ps -eF | grep java | grep photos- | awk '{ print $2 }')
echo "Shutting down process with PID: $PID"
kill $PID

exit 0
