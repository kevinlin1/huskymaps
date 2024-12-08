FROM eclipse-temurin:17-jre
RUN ./gradlew shadowJar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "build/libs/huskymaps-all.jar"]
