# Use Amazon Corretto 21 as the base image
FROM amazoncorretto:21

# Set working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY backend/target/*.jar app.jar

# Expose the port your Spring Boot app uses
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
