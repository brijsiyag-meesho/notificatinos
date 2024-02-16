# Use official Maven image as the build image
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the project object model (POM) file to the container image
COPY pom.xml .

# Copy the entire project directory to the container image
COPY src src

# Build the application with Maven
RUN mvn clean package -DskipTests

# Use a lighter weight base image for runtime
FROM openjdk:17.0

# Set the working directory in the container
WORKDIR /app

# Copy the built JAR file from the build image to the runtime image
COPY --from=build /app/target/notification-0.0.1-SNAPSHOT.jar .

ENTRYPOINT ["java","-jar","notification-0.0.1-SNAPSHOT.jar"]
