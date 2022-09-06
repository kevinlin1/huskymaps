FROM eclipse-temurin:11-jre
COPY out/artifacts/huskymaps/huskymaps.jar huskymaps.jar
CMD ["java", "-jar", "huskymaps.jar"]
