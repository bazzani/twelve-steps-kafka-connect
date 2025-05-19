plugins {
    java
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
    implementation("org.apache.kafka:kafka-clients:3.7.0")
    implementation("io.confluent:kafka-avro-serializer:7.9.0")
    implementation("org.slf4j:slf4j-simple:1.7.36")
}
