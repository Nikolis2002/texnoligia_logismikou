plugins {
    alias(libs.plugins.androidApplication)
	id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.ceid.ui"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ceid.ui"
        minSdk = 26
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
	implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.material:material:1.12.0")
}

secrets {
	// Optionally specify a different file name containing your secrets.
	// The plugin defaults to "local.properties"
	propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.defaults.properties"
}