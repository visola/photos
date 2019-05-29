#!/bin/bash

LOG_FILE=$(date +%Y%m%d%H%M%S).out

nohup java -jar $1 > $LOG_FILE 2> $LOG_FILE < /dev/null &
