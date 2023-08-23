FROM eclipse-temurin:8

LABEL maintainer="muk214782@gmail.com"

WORKDIR /app

COPY target/greenelegentfarmer-0.0.1-SNAPSHOT.jar /app/greenelegentfarmer-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "greenelegentfarmer-0.0.1-SNAPSHOT.jar"]