// plugins block using Kotlin DSL syntax
plugins {
    id("com.android.application") // Apply the Android Application plugin
    id("org.jetbrains.kotlin.android") // Apply the Kotlin Android plugin
}

// Android configuration block using Kotlin DSL syntax
android {
    // Namespace to match your actual project package path
    // Assuming your actual package structure is com.example.vgp235_final_snakegame
    namespace = "com.example.vgp235_final_snakegame"
    compileSdk = 34 // Compile against Android SDK version 34

    // Default configuration for your application
    defaultConfig {
        // Application ID: unique identifier for your app on Google Play and devices
        applicationId = "com.example.vgp235_final_snakegame"
        minSdk = 21 // Minimum Android SDK version supported
        targetSdk = 34 // Target Android SDK version for best compatibility
        versionCode = 1 // Internal version number (e.g., for updates)
        versionName = "1.0" // User-visible version name

        // Test runner for Android instrumented tests
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnit4"
    }

    // Build types configuration (e.g., release, debug)
    buildTypes {
        release {
            isMinifyEnabled = false // Disable code shrinking (ProGuard/R8) for release build
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    // Java compatibility options
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // Use Java 8 for source code
        targetCompatibility = JavaVersion.VERSION_1_8 // Target Java 8 bytecode
    }

    // Kotlin compiler options
    kotlinOptions {
        jvmTarget = "1.8" // Target JVM version 1.8 for Kotlin compilation
    }
}

// Dependencies block using Kotlin DSL syntax
dependencies {
    // Core KTX extensions for Android
    implementation("androidx.core:core-ktx:1.10.1")
    // AppCompat library for backward compatibility of UI components
    implementation("androidx.appcompat:appcompat:1.6.1")
    // Material Design components (buttons, text inputs, etc.)
    implementation("com.google.android.material:material:1.10.0")
    // ConstraintLayout for flexible UI layouts
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // RecyclerView for displaying lists of data (used in Scoreboard)
    implementation("androidx.recyclerview:recyclerview:1.3.0")

    // JUnit for local unit tests
    testImplementation("junit:junit:4.13.2")
    // AndroidX Test Ext JUnit for Android unit tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // AndroidX Espresso Core for UI testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
