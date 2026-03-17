FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/moneymantra-0.0.1-SNAPSHOT.jar moneymantra-v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java", "jar", "moneymantra-v1.0.jar"]