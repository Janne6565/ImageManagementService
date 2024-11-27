FROM eclipse-temurin:22-jammy
EXPOSE 8081
WORKDIR /app

COPY target/ImageManagementService-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "ImageManagementService-0.0.1-SNAPSHOT.jar"]