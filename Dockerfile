# Stage 1: Build the frontend
FROM node:20-alpine AS frontend-builder
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ ./
# Vite build will output to ../src/main/resources/static
RUN npm run build

# Stage 2: Build the backend
FROM maven:3.9.6-eclipse-temurin-17 AS backend-builder
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src ./src
# Copy built frontend assets from the previous stage
COPY --from=frontend-builder /app/src/main/resources/static ./src/main/resources/static
RUN mvn clean package -DskipTests

# Stage 3: Final image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=backend-builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
