import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.finalproject"
    compileSdk {
        version = release(36)
    }

    //activate build config feature
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.finalproject"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //read local.properties logic
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(FileInputStream(localPropertiesFile))
        }

        //take key. If key not found, give empty string
        val apiKey = localProperties.getProperty("GEMINI_API_KEY") ?: ""

        //create variable for usage in java
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // 1. Retrofit (Buat connect ke internet/API)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // 2. GSON Converter (Buat ubah JSON jadi Java Object)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // 3. OkHttp (Mesin pengirimnya)
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
}