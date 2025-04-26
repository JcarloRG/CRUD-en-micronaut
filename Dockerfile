FROM gradle:8.13-jdk21 AS builder
WORKDIR /home/app
COPY . .
RUN gradle build --no-daemon

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /home/app/build/libs/*-all.jar app.jar
COPY init.sql /docker-entrypoint-initdb.d/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]