plugins {
    id("java")
}

group = "de.timolia"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    compileOnly("org.powernukkit:powernukkit:1.6.0.1-PN")
    compileOnly(project(":loader"))
}

tasks.withType<Jar> {
    configurations["compileClasspath"].forEach { file: File ->
        if (file.absolutePath.contains("loader/build/libs/loader-")) {
            from(zipTree(file.absoluteFile))
        }
    }
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}