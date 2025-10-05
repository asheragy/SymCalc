plugins {
    java
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "org.cerion.symcalc"
version = "unspecified"

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm("desktop") {
        withJava()
    }

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(project(":ui:shared"))
                implementation(compose.desktop.currentOs)
                implementation(compose.preview)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
