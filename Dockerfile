FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew :auth:bootJar :catalogo:bootJar :business:bootJar :booking:bootJar :analytics:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/auth/build/libs/*.jar auth.jar
COPY --from=builder /app/catalogo/build/libs/*.jar catalogo.jar
COPY --from=builder /app/business/build/libs/*.jar business.jar
COPY --from=builder /app/booking/build/libs/*.jar booking.jar
COPY --from=builder /app/analytics/build/libs/*.jar analytics.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "auth.jar"]
