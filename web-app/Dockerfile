# pre-fetch dependencies
FROM maven:3.9.11 AS dependencies
WORKDIR /opt/app
COPY web-app/pom.xml web-app/pom.xml
COPY restful-payment/pom.xml restful-payment/pom.xml
COPY open-api/pom.xml open-api/pom.xml
COPY pom.xml .
RUN mvn -B -e org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=restful-payment,open-api

# build the jar
FROM maven:3.9.11 AS builder
WORKDIR /opt/app
#COPY --from=dependencies /root/.m2 /root/.m2
COPY --from=dependencies /opt/app/ /opt/app
COPY web-app/src /opt/app/web-app/src
RUN mvn -B -e clean install -DskipTests

# prepare runtime env
FROM openjdk:21-jdk-slim
WORKDIR /opt/app
COPY --from=builder /opt/app/web-app/target/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/app.jar"]