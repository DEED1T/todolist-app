FROM openjdk:17-jdk-alpine

RUN apk add --no-cache maven

RUN mkdir -p /data

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/prjdevops-0.0.1-SNAPSHOT.jar"]