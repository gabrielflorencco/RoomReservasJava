# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
COPY domain/pom.xml domain/
COPY application/pom.xml application/
COPY infrastructure/pom.xml infrastructure/
COPY presentation/pom.xml presentation/

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

COPY domain/src domain/src
COPY application/src application/src
COPY infrastructure/src infrastructure/src
COPY presentation/src presentation/src

RUN mvn clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S roomreservas && adduser -S roomreservas -G roomreservas
USER roomreservas

COPY --from=builder /app/presentation/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
