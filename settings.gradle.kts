pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }

    plugins {
        // Plugins for Android and Kotlin
        id("com.android.application") version "8.3.1"
        id("com.android.library") version "8.3.1"
        id("org.jetbrains.kotlin.android") version "1.9.0"
        id("com.google.gms.google-services") version "4.3.15"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
                setUrl("https://jitpack.io")
        }
        gradlePluginPortal()
    }
}



rootProject.name = "sidebar"
include(":app")