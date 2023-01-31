FROM openjdk:19-alpine

ARG APP_NAME="order-service"
ARG APP_VERSION="1.0-SNAPSHOT"
ARG JAR_FILE="target/${APP_NAME}-${APP_VERSION}.jar"

COPY ${JAR_FILE} order-service.jar
ENTRYPOINT ["java","-jar", "order-service.jar"]