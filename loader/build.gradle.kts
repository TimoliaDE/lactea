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
    compileOnly("org.projectlombok:lombok:1.18.24")
    implementation("org.javassist:javassist:3.29.0-GA")
    implementation("com.google.inject:guice:5.1.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.3")
    annotationProcessor("org.projectlombok:lombok:1.18.24")

    testCompileOnly("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
    compileOnly("com.google.auto.factory:auto-factory:1.0.1")
    annotationProcessor("com.google.auto.factory:auto-factory:1.0.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}