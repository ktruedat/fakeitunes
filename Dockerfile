FROM openjdk:15
VOLUME tmp
ADD target/fakeitunes-0.0.1-SNAPSHOT.jar fakeitunes.jar
ENTRYPOINT ["java", "-jar", "./fakeitunes.jar"]