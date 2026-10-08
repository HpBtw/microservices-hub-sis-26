# Etapa 1: build

FROM maven:3.9.8-eclipse-temurin-17 AS build

LABEL authors="hpbtw"

WORKDIR /opt/app

COPY . .

RUN mvn clean package -DskipTests

# Etapa 2: runtime

FROM eclipse-temurin:17-jre

WORKDIR /opt/app

COPY --from=build 

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]