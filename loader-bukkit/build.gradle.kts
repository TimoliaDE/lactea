plugins {
    id("java")
}

group = "de.timolia"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
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