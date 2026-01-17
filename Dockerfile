# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy project files
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Copy source code
COPY src src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create a non-root user for security (Alpine uses adduser instead of useradd)
RUN addgroup -g 1000 appuser && adduser -D -u 1000 -G appuser appuser

# Copy the JAR from builder
COPY --from=builder /app/target/*.jar app.jar

# Change ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD []
