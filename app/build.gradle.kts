plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.nrh.muzilomusicplayer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nrh.muzilomusicplayer"
        minSdk = 25
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation (libs.gson)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.compiler)
    testImplementation(libs.junit)
    implementation (libs.guava)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

