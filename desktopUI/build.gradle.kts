plugins {
    java
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.6.11"
}

group = "org.cerion.symcalc"
version = "unspecified"

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":symcalc"))
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(project(":symcalc"))
                implementation(kotlin("test-junit"))
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
