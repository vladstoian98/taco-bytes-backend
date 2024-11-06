FROM openjdk:17-jdk-alpine

WORKDIR /app

VOLUME /tmp

COPY target/TacoCloudApplication-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar", "--server.port=8080"]
