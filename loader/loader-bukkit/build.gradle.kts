plugins {
    id("java")
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
    implementation(project(":core"))
}

tasks.withType<Jar> {
    configurations["compileClasspath"].forEach { file: File ->
        if (file.absolutePath.contains("initialize/build/libs/initialize-")) {
            from(zipTree(file.absoluteFile))
        }
    }
}