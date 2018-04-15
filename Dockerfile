FROM openjdk:8-jre-alpine
VOLUME /tmp

# argument comes from pom.xml file when building
ARG JAR_FILE
COPY target/fhir-adaptor.jar fhir-adaptor.jar

# urandom makes tomcat start up much faster
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-jar","/fhir-adaptor.jar"]
