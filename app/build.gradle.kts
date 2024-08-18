plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.example.nasaimagesearch20"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.nasaimagesearch20"
        minSdk = 21
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
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.okhttp)
    implementation(libs.gson)
    implementation(libs.picasso)
    implementation(libs.cardview)
    implementation(libs.glide)
//    kapt("com.github.bumptech.glide:compiler:4.14.2") // Change this to kapt


}