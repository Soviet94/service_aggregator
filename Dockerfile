# Stage 1: Build the application
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew gradlew.bat /app/
COPY gradle /app/gradle
COPY src /app/src

RUN ./gradlew clean build -x test

# Stage 2: Runtime image
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/build/libs/service_aggregator-0.0.1-SNAPSHOT.jar app.jar

# Expose your configured port
EXPOSE 8106

ENTRYPOINT ["java", "-jar", "/app/app.jar"]