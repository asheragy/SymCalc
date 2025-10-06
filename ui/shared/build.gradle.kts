plugins {
    java
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "org.cerion.symcalc"
version = "unspecified"

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(project(":symcalc"))
    implementation(compose.runtime)
    implementation(compose.foundation)
    // TODO material3
    implementation(compose.material)

    testImplementation(kotlin("test-junit5"))
}

tasks.test {
    useJUnitPlatform()
}
