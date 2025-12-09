plugins {
    // Sintaxis correcta para archivos .kts
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    namespace = "com.example.mantenimientovehiculos"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mantenimientovehiculos"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    // Habilitar ViewBinding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Gson Converter: para convertir objetos Kotlin a JSON y viceversa
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // (Opcional pero recomendado) OkHttp Logging Interceptor: para ver las llamadas de red en el Logcat
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    // --- FIN DE DEPENDENCIAS PARA RETROFIT ---

    // --- LIBRERÍAS BÁSICAS DE ANDROIDX ---
    // Aquí usamos los alias corregidos del archivo libs.versions.toml
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    // El alias correcto y único para la librería de "activity"
    implementation(libs.androidx.activity.ktx)

    // --- ARQUITECTURA Y CICLO DE VIDA (ViewModel, LiveData) ---
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // --- ROOM (BASE DE DATOS) ---
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler) // "kapt" para el procesador de anotaciones

    // --- COROUTINES (HILOS SECUNDARIOS) ---
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // --- LIBRERÍAS DE TESTING ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // --- LIBRERÍAS QUE NO ESTABAN EN LIBS.VERSIONS.TOML ---
    // (No necesitamos añadir `retrofit` aquí si no lo vas a usar todavía)
}
