FROM openjdk:17.0-jdk

ENV HOME /chartographer

WORKDIR "$HOME"

ARG JAR=web/target/web-1.0-SNAPSHOT.jar
ARG JAR_PROPS=web/src/main/resources/application.yml
ARG LOGGING_CONFIG=web/src/main/resources/logging-config.xml

COPY ${JAR} web-1.0-SNAPSHOT.jar
COPY ${JAR_PROPS} application.yml
COPY ${LOGGING_CONFIG} logging-config.xml

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005", "-jar", "/chartographer/web-1.0-SNAPSHOT.jar", "./", "--spring.config.import=file:/chartographer/application.yml"]
