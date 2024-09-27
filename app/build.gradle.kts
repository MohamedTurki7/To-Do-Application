plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")


}

android {
    namespace = "com.route.todoapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.route.todoapplication"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    androidResources {
        generateLocaleConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)           // Already using androidx.core:core-ktx
    implementation(libs.androidx.appcompat)          // AppCompat in AndroidX
    implementation(libs.material)                    // Material Design components from AndroidX
    testImplementation(libs.junit)                   // JUnit testing
    androidTestImplementation(libs.androidx.junit)   // AndroidX JUnit
    androidTestImplementation(libs.androidx.espresso.core)  // Espresso for UI testing

    // Room dependencies
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    // LeakCanary for memory leak detection in debug builds
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")

    // Swipe to delete functionality using SwipeLayout library
    implementation("com.github.zerobranch:SwipeLayout:1.3.1")

    // CalendarView library
    implementation(libs.material.calendarview)

    // Core KTX (already included in libs.androidx.core.ktx)
    val core_version = "1.13.1"
    implementation("androidx.core:core-ktx:$core_version")
}
