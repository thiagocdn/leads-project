FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

# Gradle Dependencies
COPY gradle gradle
COPY gradlew ./
COPY build.gradle.kts ./
COPY settings.gradle.kts ./
COPY gradlew ./
RUN ./gradlew dependencies --no-daemon

# Build the application
COPY . .
RUN ./gradlew clean build -x test

EXPOSE 8080

CMD ["java", "-jar", "./build/libs/leads-0.0.1-SNAPSHOT.jar"]