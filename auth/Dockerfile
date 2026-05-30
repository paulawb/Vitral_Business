FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

RUN apt-get update && apt-get install -y unzip curl \
    && curl -fsSL https://services.gradle.org/distributions/gradle-8.10-bin.zip -o gradle.zip \
    && unzip gradle.zip \
    && rm gradle.zip gradle.zip \
    && apt-get remove -y unzip curl && apt-get autoremove -y && apt-get clean

ENV GRADLE_HOME=/app/gradle-8.10
ENV PATH=$GRADLE_HOME/bin:$PATH

COPY . .
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
