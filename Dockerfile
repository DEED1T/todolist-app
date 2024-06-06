FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]