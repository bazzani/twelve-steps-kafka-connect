plugins {
    java
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("io.freefair.lombok") version "8.6"
    id("com.avast.gradle.docker-compose") version "0.17.12"
}

group = "jjug.kafka.connect"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven {
        name = "confluent"
        url = uri("https://packages.confluent.io/maven/")
    }
}

dependencies {
    val kafkaVersion = "7.9.0"

    implementation(project(":kafka-connect-smt-lib"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.confluent:kafka-avro-serializer:$kafkaVersion")

    runtimeOnly("org.postgresql:postgresql")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    dependsOn(tasks.named("composeUp"))
    finalizedBy(tasks.named("composeDown"))
}

dockerCompose {
    projectNamePrefix = "twelve-steps-kafka-connect-int-test"
    startedServices = setOf("broker", "connect", "schema-registry", "postgres")

    captureContainersOutputToFiles = project.layout.buildDirectory.dir("container-logs")
    retainContainersOnStartupFailure = true

    dockerExecutable = "/usr/local/bin/docker"
}
