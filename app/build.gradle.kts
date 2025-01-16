plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    id("com.google.devtools.ksp")
//    id("com.google.dagger.hilt.android.plugin")
}

//apply(from = "license.check.gradle.kts") // Apply the license validation script

android {
    namespace = "com.example.graphicsmaker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.graphicsmaker"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDir("build/generated/ksp/debug/kotlin")
        }
    }
}

dependencies {

    implementation(project(":library"))
    implementation(project(":cc_ads"))
    implementation(project(":shiftcolorpicker"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.media:media:1.0.0"){
        exclude( group = "com.android.support", module = "support-media-compat")
    }

    implementation("androidx.compose.foundation:foundation:1.7.3")
    implementation("com.github.markushi:android-ui:1.2")
    implementation("com.astuetz:pagerslidingtabstrip:1.0.1")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.h6ah4i.android.widget.verticalseekbar:verticalseekbar:0.7.2")
    implementation("com.github.woxthebox:draglistview:1.6.4")
    implementation("it.neokree:MaterialTabs:0.11")
    implementation("com.github.yukuku:ambilwarna:2.0.1")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.work:work-runtime:2.7.1")
    implementation("androidx.legacy:legacy-support-v13:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.10.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.apache.httpcomponents:httpclient-android:4.3.5.1")
    implementation("com.google.android.gms:play-services-ads:23.5.0")
    implementation("com.android.billingclient:billing:7.1.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose-android:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-android:2.8.7")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.activity:activity-ktx:1.7.2")


//    val roomVersion = "2.5.2" // Use the latest Room version
//
//    implementation("androidx.room:room-runtime:$roomVersion")
//    implementation("androidx.room:room-ktx:$roomVersion") // For Kotlin extensions
//    ksp("androidx.room:room-compiler:$roomVersion") // Use KSP instead of KAPT

    // Hilt dependencies
//    implementation("com.google.dagger:hilt-android:2.44")
//    ksp("com.google.dagger:hilt-compiler:2.44")

}