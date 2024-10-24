FROM node:22 AS frontend-build
WORKDIR /app/frontend

COPY ./frontend/package*.json ./

RUN npm install

COPY ./frontend ./

RUN npm run build

FROM gradle:8-jdk21 AS backend-build
WORKDIR /home/gradle

COPY settings.gradle.kts gradlew ./
COPY gradle gradle/
COPY backend/build.gradle.kts ./

RUN ./gradlew bootJar --no-daemon

COPY backend/ ./
COPY --from=frontend-build /app/frontend/build/ backend/src/main/resources/static/

RUN ./gradlew bootJar
RUN mv build/libs/*.jar app.jar

FROM eclipse-temurin:21-alpine

RUN addgroup -g 1000 app && adduser -G app -D -u 1000 -h /app app

USER app

WORKDIR /app

COPY --from=backend-build --chown=1000:1000 /home/gradle/app.jar .

CMD ["java", "-jar", "app.jar"]
