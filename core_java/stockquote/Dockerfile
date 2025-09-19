# Use official OpenJDK image as base
FROM openjdk:17-jdk-slim

# Set work directory inside container
WORKDIR /stockquote

# Copy your JAR into the container
COPY target/stockquote-app-1.0-SNAPSHOT.jar app.jar

# Run the JAR when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]

