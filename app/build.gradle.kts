plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.example.sidebar"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.sidebar"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/ASL2.0"
            )
        }
    }
}

dependencies {
    // Core Android Libraries (Only AndroidX)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)

    // Google Play Services Auth
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Google API Client
    implementation("com.google.api-client:google-api-client-android:1.35.2")
    implementation("com.google.api-client:google-api-client-gson:1.35.2")
    implementation("com.google.http-client:google-http-client-gson:1.42.3")
    implementation("com.google.http-client:google-http-client-android:1.42.3")
    // Google Calendar API
    implementation("com.google.apis:google-api-services-calendar:v3-rev411-1.25.0")

    // Material Calendar View (correct version)
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.0")

    implementation("com.github.prolificinteractive:material-calendarview:1.4.3")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

configurations.all {
    resolutionStrategy {
        force("androidx.core:core-ktx:1.13.0")
        force("androidx.appcompat:appcompat:1.6.1")
        force("androidx.constraintlayout:constraintlayout:2.1.4")

        // Exclure les dépendances conflictuelles
        exclude(module = "support-compat")
        exclude(module = "support-core-utils")
        exclude(module = "support-core-ui")
        exclude(module = "support-fragment")
    }
}

