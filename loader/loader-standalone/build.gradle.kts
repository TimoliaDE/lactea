plugins {
    id("java")
}

dependencies {
    implementation(project(":core"))
}

tasks.withType<Jar> {
    configurations["compileClasspath"].forEach { file: File ->
        if (file.absolutePath.contains("initialize/build/libs/initialize-")) {
            from(zipTree(file.absoluteFile))
        }
    }
}