FROM openjdk
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/compose-postgres
ENV SPRING_DATASOURCE_USERNAME=compose-postgres
ENV SPRING_DATASOURCE_PASSWORD=compose-postgres
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV DISCORD_TOKEN=compose-postgres
COPY ./target/Luna*.jar Luna.jar
ENTRYPOINT ["java", "-jar", "Luna.jar"]