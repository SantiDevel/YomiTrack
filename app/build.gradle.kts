plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.santiparra.yomitrack"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.santiparra.yomitrack"
        minSdk = 24
        targetSdk = 35
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

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Room
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")

    /*Dependecia para usar glide y cargar imagenes mediante url*/
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // Retrofit para llamadas HTTP
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Convertidor Gson (JSON <-> Java)
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // (Opcional) Logging para depurar peticiones
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
}