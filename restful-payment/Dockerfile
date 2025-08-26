# pre-fetch dependencies
FROM maven:3.9.11 AS dependencies
WORKDIR /opt/app
COPY restful-payment/pom.xml restful-payment/pom.xml
COPY web-app/pom.xml web-app/pom.xml
COPY open-api/pom.xml open-api/pom.xml
COPY pom.xml .
RUN mvn -B -e org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=web-app,open-api

# build the jar
FROM maven:3.9.11 AS builder
WORKDIR /opt/app
#COPY --from=dependencies /root/.m2 /root/.m2
COPY --from=dependencies /opt/app/ /opt/app
COPY restful-payment/src /opt/app/restful-payment/src
RUN mvn -B -e clean install -DskipTests

# prepare runtime env
FROM openjdk:21-jdk-slim
WORKDIR /opt/app
COPY --from=builder /opt/app/restful-payment/target/*.jar /app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]


