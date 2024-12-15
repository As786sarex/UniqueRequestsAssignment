# Stage 1: Build
FROM openjdk:17.0.1-jdk-slim

WORKDIR /app

# Copy Gradle files
COPY build.gradle settings.gradle gradlew ./

# Copy source files
COPY src ./src

COPY gradle ./gradle

# Run the build
RUN ./gradlew wrapper clean build

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "./build/libs/uniquerequests-1.0.1.jar"]
