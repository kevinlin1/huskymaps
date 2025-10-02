import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("com.gradleup.shadow") version "9.2.2"
    java
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    implementation("commons-codec:commons-codec:1.19.0")
    implementation("io.javalin:javalin:6.7.0")
    implementation("org.locationtech.spatial4j:spatial4j:0.8")
    implementation("org.slf4j:slf4j-simple:2.0.17")

    testImplementation("org.junit.jupiter:junit-jupiter:6.0.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        showExceptions = true
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:unchecked")
    options.compilerArgs.add("-Xlint:deprecation")
    options.compilerArgs.add("-Werror")
}

tasks.jar {
    manifest.attributes["Main-Class"] = "MapServer"
}
