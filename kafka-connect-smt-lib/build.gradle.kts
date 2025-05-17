plugins {
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    id("io.freefair.lombok") version "8.6"
}

group = "jjug.kafka.connect"
version = "0.0.1-SNAPSHOT"

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val connectVersion = "3.9.0"
    val jsonVersion = "20250107"

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("org.apache.kafka:connect-api:$connectVersion")

    // These dependencies are used internally, and not exposed to consumers on their own compile classpath.
    implementation("org.apache.kafka:connect-transforms:$connectVersion")

    implementation("org.json:json:$jsonVersion")
    implementation("org.slf4j:slf4j-api:1.7.36")

    // Use JUnit Jupiter for testing.
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testImplementation("org.skyscreamer:jsonassert:1.5.3")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
