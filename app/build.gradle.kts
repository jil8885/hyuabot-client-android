plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.hiltPlugin)
    alias(libs.plugins.apollo)
}

android {
    namespace = "app.kobuggi.hyuabot"
    compileSdk = 34

    apollo {
        service("query") {
            packageName = "app.kobuggi.hyuabot"
        }
    }

    defaultConfig {
        applicationId = "app.kobuggi.hyuabot"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    tasks.withType(JavaCompile::class.java) {
        options.compilerArgs.addAll(
            listOf(
                "-Xlint:unchecked",
                "-Xlint:deprecation",
            ),
        )
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    // Navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.navigation.dynamic.features.fragment)
    // RXJava
    implementation(libs.rxJava)
    implementation(libs.rxAndroid)
    // Networking
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.apollo)
    // DataStore
    implementation(libs.dataStore)
    implementation(libs.dataStoreRx)
    // ViewPager2
    implementation(libs.viewPager2)
    // Room
    implementation(libs.room)
    implementation(libs.roomRx)
    implementation(libs.roomKtx)
    implementation(libs.roomPaging)
    ksp(libs.roomCompiler)
    // Calendar
    implementation(libs.calendar)
    // Firebase
    implementation(platform(libs.firebase))
    implementation(libs.firebaseMessaging)
    implementation(libs.firebaseAnalytics)
    implementation(libs.firebaseCrashlytics)
    // Map
    implementation(libs.kakaoMap)
}

hilt {
    enableAggregatingTask = true
}