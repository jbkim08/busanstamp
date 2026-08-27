# ---------------------------
# 1. Build
# ---------------------------
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY . .

RUN chmod +x gradlew

RUN ./gradlew clean bootJar -x test


# ---------------------------
# 2. Run
# ---------------------------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]