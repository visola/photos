#!/bin/bash

LOG_FILE=$(date +%Y%m%d%H%M%S).out

rm -Rf *.out

echo "Starting application and redirecting log to $LOG_FILE"
nohup java -jar $1 > $LOG_FILE 2> $LOG_FILE < /dev/null &
