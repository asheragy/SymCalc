plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "org.cerion.symcalc"
version = "unspecified"

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":ui:shared"))
                implementation(project(":symcalc"))
                implementation(compose.desktop.currentOs)
                implementation(compose.preview)
                implementation("org.scilab.forge:jlatexmath:1.0.7")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}