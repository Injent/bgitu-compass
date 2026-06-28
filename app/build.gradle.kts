import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    id("kotlin-parcelize")
}

android {
    namespace = "ru.bgitu.app"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "ru.bgitu.app"
        minSdk = 26
        targetSdk {
            version = release(36)
        }
        versionCode = 17
        versionName = "19.6.26"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions.add("distribution")
    productFlavors {
        create("standalone") {
            dimension = "distribution"
        }
        create("rustore") {
            dimension = "distribution"
        }
    }

    signingConfigs {
        create("release") {
            val keystorePropertiesFile = rootProject.file("cert/release.properties")
            require(keystorePropertiesFile.exists()) {
                "Signing config not found"
            }
            val props = Properties()
            props.load(keystorePropertiesFile.inputStream())

            storeFile = rootProject.file(props.getProperty("storeFile"))
            storePassword = props.getProperty("storePassword")
            keyAlias = props.getProperty("keyAlias")
            keyPassword = props.getProperty("keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            resValue("string", "app_name", "Compass Debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
            optIn.addAll(
                "kotlinx.coroutines.ExperimentalCoroutinesApi"
            )
        }
    }
    composeCompiler {
        stabilityConfigurationFiles.add {
            rootProject.layout.projectDirectory.file("stability_config.conf").asFile
        }
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

val rustoreImplementation = "rustoreImplementation"

dependencies {
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive)

    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.splashScreen)
    implementation(libs.androidx.browser)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // DataStore
    implementation(libs.androidx.datastore)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    // Glance
    implementation(libs.glance.widget)
    implementation(libs.glance)
    debugImplementation(libs.glance.preview)
    debugImplementation(libs.glance.appwidget.preview)
    implementation(libs.glance.appwidget.host)

    // KotlinX
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.immutableCollections)

    // Ktor
    implementation(libs.ktor.core)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.contentNegotiation)
    implementation(libs.ktor.kotlinx.json)

    // Navigation 3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor3)
    implementation(libs.coil.gif)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)

    // RuStore
    rustoreImplementation(platform(libs.rustore.bom))
    rustoreImplementation(libs.rustore.appupdate)

    // Other libraries
    implementation(libs.apiresult)
    implementation(libs.reorderable)
    implementation(libs.haze)
    implementation(libs.haze.materials)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
