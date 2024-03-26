FROM eclipse-temurin:17-jre
COPY out/artifacts/projects/projects.jar projects.jar
CMD ["java", "-jar", "projects.jar"]
