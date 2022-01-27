FROM openjdk:17.0.2

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

COPY Lyra.jar Lyra.jar

ENTRYPOINT ["/bin/sh", "-c", "java -jar Lyra.jar"]