FROM amazoncorretto:11

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENV VACATION_SERVICE_JWT_SECRET_KEY=$secret_key

ENTRYPOINT ["java","-jar","/app.jar"]
