FROM openjdk:19

ARG APP_NAME="payment-service"
ARG APP_VERSION="1.0-SNAPSHOT"
ARG JAR_FILE="target/${APP_NAME}-${APP_VERSION}.jar"
COPY ${JAR_FILE} payment-service.jar
ENTRYPOINT ["java","-jar", "payment-service.jar"]

