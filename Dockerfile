FROM node:22 AS frontend-build
WORKDIR /app/frontend

COPY ./frontend/package*.json ./

RUN npm install

COPY ./frontend ./

RUN npm run build

FROM nginx:latest AS frontend
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=frontend-build /app/frontend/build /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

FROM gradle:8-jdk21 AS backend-build
WORKDIR /home/gradle

COPY settings.gradle.kts gradlew ./
COPY gradle gradle/
COPY backend/build.gradle.kts ./

RUN gradle bootJar --no-daemon

COPY backend/ ./
COPY --from=frontend-build /app/frontend/build/ backend/src/main/resources/static/


RUN gradle bootJar
RUN mv build/libs/*.jar app.jar

FROM eclipse-temurin:21-alpine

RUN addgroup -g 1000 app && adduser -G app -D -u 1000 -h /app app


USER app

WORKDIR /app

COPY --from=backend-build --chown=1000:1000 /home/gradle/app.jar .

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
