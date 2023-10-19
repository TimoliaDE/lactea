plugins {
    `java-library`
    kotlin("jvm") version "1.9.10"
}

group = "timolia.lactea"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":module-api"))
    api(project(":initialize"))
    implementation("org.javassist:javassist:3.29.0-GA")
    implementation(kotlin("stdlib-jdk8"))
    compileOnlyApi("com.google.auto.factory:auto-factory:1.0.1")
    annotationProcessor("com.google.auto.factory:auto-factory:1.0.1")
}
kotlin {
    jvmToolchain(11)
}