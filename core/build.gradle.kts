plugins {
    `java-library`
    kotlin("jvm") version "1.9.10"
}

dependencies {
    api(project(":module-api"))
    compileOnlyApi(project(":initialize"))
    implementation("org.javassist:javassist:3.29.0-GA")
    implementation(kotlin("stdlib-jdk8"))
}
kotlin {
    jvmToolchain(11)
}