# Use a base image with JDK and Maven installed
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven configuration files
COPY pom.xml .
COPY src ./src

# Build the application using Maven
RUN mvn package -DskipTests

# Use a lightweight base image with JRE to run the application
FROM adoptopenjdk/openjdk17:jre-17.0.1_12-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage to the container
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar .

# Expose the port that the Spring Boot application will run on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]