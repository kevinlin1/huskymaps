FROM gradle:latest AS build
COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar

FROM eclipse-temurin:17-jre
COPY --from=build /home/gradle/src/build/libs/huskymaps-all.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
