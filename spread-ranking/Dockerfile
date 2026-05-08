# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy everything so Maven can see the multi-module structure
COPY . .

# ARG must be declared in Stage 1 to be available for the RUN command
ARG SERVICE_NAME

# Build the common contract and the selected service
# (-pl: project list, -am: also make dependencies)
# Updated common-lib to common-contracts to match your structure
RUN mvn clean package -DskipTests -pl common-contracts,${SERVICE_NAME} -am

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Re-declare ARG in Stage 2 to use it in the COPY command
ARG SERVICE_NAME

# Copy the JAR from the appropriate module
COPY --from=build /app/${SERVICE_NAME}/target/*.jar app.jar

# Using ZGC (Z Garbage Collector) is an excellent choice for Java 21 and microservices!
ENTRYPOINT ["java", "-XX:+UseZGC", "-jar", "app.jar"]
