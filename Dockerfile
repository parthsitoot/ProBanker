# syntax=docker/dockerfile:1
# Multi-stage Dockerfile for a Spring Boot application with Maven build

# Build stage: use Maven with JDK to compile and package the app
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only the files needed for dependency download first to leverage Docker layer caching
COPY pom.xml .
COPY .mvn/ .mvn
COPY mvnw mvnw
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -B

# Copy source and run the package phase
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -DskipTests package

# Runtime stage: minimal JRE image
FROM eclipse-temurin:17-jre-jammy AS runtime


WORKDIR /app

# Copy the packaged jar from the build stage (uses wildcard to match artifact)
COPY --from=build /app/target/*.jar /app/app.jar
RUN chown -R ${APP_USER}:appgroup /app

USER ${APP_USER}

# Expose the app port (adjust if your app uses a different port)
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]