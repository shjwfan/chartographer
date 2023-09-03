FROM openjdk:17.0-jdk
ENV HOME /chartographer
WORKDIR "$HOME"
ARG JAR=web/target/web-1.0-SNAPSHOT.jar
ARG JAR_PROPS=web/src/main/resources/application.yml
COPY ${JAR} web-1.0-SNAPSHOT.jar
COPY ${JAR_PROPS} application.yml
ENTRYPOINT ["java", "-jar", "/chartographer/web-1.0-SNAPSHOT.jar", "./", "--spring.config.import=file:/chartographer/application.yml"]
