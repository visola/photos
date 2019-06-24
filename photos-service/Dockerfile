FROM openjdk:8-jre-alpine

ARG JAR_FILE

COPY $JAR_FILE app.jar
ENTRYPOINT ["/usr/bin/java", "-jar", "app.jar"]
