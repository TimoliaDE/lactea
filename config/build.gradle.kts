plugins {
    `java-library`
    kotlin("jvm") version "1.9.10"
}

dependencies {
    api(project(":core"))
    implementation(kotlin("stdlib-jdk8"))
    annotationProcessor("com.google.auto.factory:auto-factory:1.0.1")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.3")
}
kotlin {
    jvmToolchain(11)
}