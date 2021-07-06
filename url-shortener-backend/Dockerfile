FROM openjdk:11.0.4-jre-slim

WORKDIR /app

COPY build/libs/url-shortener-backend-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
