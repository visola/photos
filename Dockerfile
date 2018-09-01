FROM openjdk:8-jdk-alpine

COPY build/libs/life-booster.jar /

COPY src/main/resources/templates /templates
RUN touch /templates/*

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "life-booster.jar"]
