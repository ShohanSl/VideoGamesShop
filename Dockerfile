FROM node:24-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
COPY src/main/resources/static /app/src/main/resources/static
RUN npm run build

FROM maven:3.9-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
RUN mvn -B -DskipTests dependency:go-offline
COPY src ./src
COPY --from=frontend-build /app/src/main/resources/static ./src/main/resources/static
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY --from=backend-build /app/target/*.jar app.jar
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
EXPOSE 10000
ENV PORT=10000
ENV JAVA_OPTS=""
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD curl -fsS "http://localhost:${PORT:-10000}/actuator/health" || exit 1
ENTRYPOINT ["sh", "/app/docker-entrypoint.sh"]
