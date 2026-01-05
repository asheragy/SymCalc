plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.application")
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
        val desktopMain by getting {
            dependencies {
                implementation(project(":ui:shared"))
                implementation(project(":symcalc"))
                implementation(compose.desktop.currentOs)
                implementation(compose.preview)
                implementation("org.scilab.forge:jlatexmath:1.0.7")
            }
        }

        androidMain.dependencies {
            implementation(project(":ui:shared"))

            // Activity + setContent() + ComponentActivity
            implementation("androidx.activity:activity-compose:1.9.3")

            // Jetpack Compose on Android
            //implementation("androidx.compose.ui:ui:1.7.2")
            implementation("androidx.compose.ui:ui-tooling-preview:1.7.2")
            implementation("androidx.compose.ui:ui-tooling:1.7.2")
            implementation("androidx.compose.foundation:foundation:1.7.2")
            implementation("androidx.compose.material:material:1.7.2")
            // Optional but handy
            //implementation("androidx.core:core-ktx:1.13.1")

            implementation(compose.preview)
        }
    }
}


android {
    namespace = "org.cerion.symcalc.ui"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.cerion.symcalc.ui"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
dependencies {
    debugImplementation("androidx.compose.ui:ui-tooling:1.9.2")
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
