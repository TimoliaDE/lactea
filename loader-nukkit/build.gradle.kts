plugins {
    id("java")
}

group = "de.timolia"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    compileOnly("org.powernukkit:powernukkit:1.6.0.1-PN")
    implementation(project(":loader"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}