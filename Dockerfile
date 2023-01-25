FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY gradle ./gradle
COPY gradlew build.gradle settings.gradle ./
COPY src ./src
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build

CMD ["java", "-jar", "discord-bot-0.0.1.jar"]