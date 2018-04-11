FROM openjdk:8-jdk-alpine
VOLUME /tmp

# argument comes from pom.xml file when building
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# urandom makes tomcat start up much faster
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

