plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "twelve-steps-kafka-connect"

include("kafka-consumer-spring-boot-jdbc")
include("kafka-consumer-sprint-0")
