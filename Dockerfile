FROM openjdk:8-jre-alpine

ARG FILES_DIR

COPY $FILES_DIR /
ENTRYPOINT ["/usr/bin/bash", "bin/photos" ]
