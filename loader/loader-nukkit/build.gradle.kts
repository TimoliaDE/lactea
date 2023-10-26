plugins {
    id("java")
}

dependencies {
    implementation("org.powernukkit:powernukkit:1.6.0.1-PN")
    implementation(project(":core"))
}

tasks.withType<Jar> {
    configurations["compileClasspath"].forEach { file: File ->
        if (file.absolutePath.contains("initialize/build/libs/initialize-")) {
            from(zipTree(file.absoluteFile))
        }
    }
}