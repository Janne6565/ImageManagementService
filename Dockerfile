FROM eclipse-temurin:22-jammy
EXPOSE 8080
WORKDIR /app

COPY target/SyncUpV2-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "SyncUpV2-0.0.1-SNAPSHOT.jar"]