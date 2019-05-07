FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ARG ACTIVE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=$ACTIVE","-jar","/app.jar"]