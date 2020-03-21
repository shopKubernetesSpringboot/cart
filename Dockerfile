FROM openjdk:8-jre-alpine
#FROM openjdk:11-jdk-slim
#FROM adoptopenjdk/openjdk9
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]