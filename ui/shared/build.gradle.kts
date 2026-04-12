plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "org.cerion.symcalc"
version = "unspecified"

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm("desktop")

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    sourceSets {
        val jvmSharedMain by creating {
            //dependsOn(commonMain)
            dependencies {
                // Java / Kotlin-JVM math library
                implementation(project(":symcalc"))

                // Shared Compose UI
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                // or material3 if you prefer
            }
        }

        val jvmSharedTest by creating {
            //dependsOn(commonTest)
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val desktopMain by getting {
            dependsOn(jvmSharedMain)
            dependencies {
                implementation(project(":symcalc"))
                implementation(compose.desktop.currentOs)
                implementation(compose.preview)
                implementation("org.scilab.forge:jlatexmath:1.0.7")
            }
        }

        val desktopTest by getting {
            dependsOn(jvmSharedTest)
        }

        val androidMain by getting {
            dependsOn(jvmSharedMain)
        }
    }
}


android {
    namespace = "org.cerion.symcalc.ui"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
