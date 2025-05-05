FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/*.jar /app/my-online-store-reactive.jar
ENTRYPOINT ["java","-jar","/app/my-online-store-reactive.jar"]