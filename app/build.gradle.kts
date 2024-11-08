plugins {
    alias(libs.plugins.android.application)
    // Không cần alias cho google.gms.google.services nếu không sử dụng Firebase
}

android {
    namespace = "com.example.shopgame"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shopgame"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Các dependencies chính của ứng dụng
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Thư viện hiển thị hình ảnh
    implementation("com.squareup.picasso:picasso:2.71828")

    // Room Database dependencies
    implementation("androidx.room:room-runtime:2.4.2") // Kiểm tra phiên bản mới nhất nếu cần
    annotationProcessor("androidx.room:room-compiler:2.4.2") // Dành cho Java; với Kotlin dùng kapt

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    implementation ("com.facebook.stetho:stetho:1.5.1")
    implementation ("com.google.android.gms:play-services-maps:18.0.2")

}

// Không cần `apply plugin: 'com.google.gms.google-services'` nếu không dùng Firebase
