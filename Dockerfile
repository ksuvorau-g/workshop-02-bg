FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copy Maven files and source
COPY pom.xml .
COPY src ./src

# Build application (will work when network is available)
RUN mvn clean package -DskipTests && \
    mv target/*.jar app.jar

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/app.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
