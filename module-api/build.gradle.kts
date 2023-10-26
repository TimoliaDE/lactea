plugins {
    `java-library`
    kotlin("jvm") version "1.9.10"
}
dependencies {
    api("com.google.inject:guice:5.1.0")
    implementation(kotlin("stdlib-jdk8"))
}
kotlin {
    jvmToolchain(11)
}