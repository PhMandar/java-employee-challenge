# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jdk-slim

# Set the maintainer label
LABEL maintainer="mandyce2006@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# The application's jar file
ARG JAR_FILE=build/libs/*.jar

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]
