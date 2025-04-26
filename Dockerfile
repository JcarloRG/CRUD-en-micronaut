# Etapa de construcción
FROM gradle:8.13-jdk21 AS builder
WORKDIR /home/app
COPY . .
RUN gradle build --no-daemon

# Etapa de ejecución
FROM eclipse-temurin:21-jdk  
WORKDIR /app
COPY --from=builder /home/app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
