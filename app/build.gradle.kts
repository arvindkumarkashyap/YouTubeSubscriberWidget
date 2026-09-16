plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.arvind.subscriberwidget"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.arvind.subscriberwidget"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        val youtubeApiKey =
    System.getenv("YOUTUBE_API_KEY")
        ?: project.findProperty("YOUTUBE_API_KEY")?.toString()
        ?: ""

buildConfigField(
    "String",
    "YOUTUBE_API_KEY",
    "\"$youtubeApiKey\""
)
    }
    buildFeatures { compose = true; buildConfig = true }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2025.08.01"))
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.2")
    implementation("androidx.glance:glance-appwidget:1.2.0")
    implementation("androidx.work:work-runtime-ktx:2.10.3")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
}
