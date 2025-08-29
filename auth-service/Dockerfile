FROM openjdk:17-jdk-alpine

# Set environment
ENV APP_HOME=/app
WORKDIR $APP_HOME

# Copy app JAR
COPY target/*.jar app.jar

# Expose port (adjust per service)
EXPOSE 9092

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]