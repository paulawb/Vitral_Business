FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
COPY auth/build.gradle ./auth/
RUN chmod +x gradlew && ./gradlew :auth:dependencies --no-daemon || true

COPY . .
RUN chmod +x gradlew && ./gradlew :auth:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/auth/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
#hola mundo