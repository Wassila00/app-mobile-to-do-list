// Top-level build file where you can add configuration options common to all sub-projects/modules.


plugins {

        // Utilisez soit "alias(libs.plugins.android.application)" soit "com.android.application" mais pas les deux
        alias(libs.plugins.android.application) apply false
        id("org.jetbrains.kotlin.android") version "1.9.0" apply false
        id("com.android.library") version "8.3.1" apply false
        id("com.google.gms.google-services") version "4.3.15" apply false


}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

