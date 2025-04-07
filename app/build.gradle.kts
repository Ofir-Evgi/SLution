plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.slution"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.slution"
        minSdk = 26
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
    implementation (libs.material.v190)
    implementation (libs.recyclerview)
    implementation (libs.material.v1110)
    implementation (libs.cardview)
    implementation (libs.camera.core)
    implementation (libs.camera.camera2)
    implementation (libs.camera.lifecycle)
    implementation (libs.camera.view)
    implementation (libs.material.v1120)
    implementation (libs.camera.core.v130)
    implementation (libs.camera.camera2.v130)
    implementation (libs.camera.lifecycle.v130)
    implementation (libs.camera.view.v130)


}