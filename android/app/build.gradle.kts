plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.walkmate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.walkmate"
        minSdk = 26                 // Android 8.0 — Health Connect 최소
        targetSdk = 34              // Android 14 — FGS Type 강제 적용 대상
        versionCode = 1
        versionName = "0.1.0-prep"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }

        // BuildConfig에 백엔드 URL 노출 (디버그/릴리스 분기 가능)
        buildConfigField("String", "API_BASE_URL", "\"https://walkmate-api.onrender.com/\"")
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            // NCP 콘솔에 com.walkmate.debug 패키지명도 별도 등록 필요 (android-naver-sdk 보고서 §11)
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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

    kotlinOptions { jvmTarget = "17" }

    buildFeatures {
        viewBinding = true
        compose = true
        buildConfig = true
    }

    packaging {
        resources.excludes += setOf(
            "META-INF/AL2.0", "META-INF/LGPL2.1",
            "/META-INF/{AL2.0,LGPL2.1}",
            "META-INF/LICENSE*", "META-INF/NOTICE*"
        )
    }
}

dependencies {
    // Core
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)

    // Activity / Fragment / Navigation
    implementation(libs.activity.ktx)
    implementation(libs.activity.compose)
    implementation(libs.fragment.ktx)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.fragment)
    implementation(libs.hilt.navigation.compose)

    // Room (Week 3)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Retrofit / Moshi (Week 6-7)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.okhttp.logging)

    // WorkManager (Week 6)
    implementation(libs.work.runtime.ktx)

    // Sensors / Location / Maps (Week 2-5)
    implementation(libs.health.connect)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)

    // Compose (Week 8 1화면)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    debugImplementation(libs.compose.ui.tooling)

    // UI helpers
    implementation(libs.glide)
    implementation(libs.mpandroidchart)

    // Debug
    debugImplementation(libs.leak.canary)

    // Test
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}
