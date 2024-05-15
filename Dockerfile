FROM amazoncorretto:22-alpine-jdk

WORKDIR /app

RUN apk add --no-cache curl

COPY target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]