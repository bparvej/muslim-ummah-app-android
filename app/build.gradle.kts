import java.util.Properties

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.muslimummah"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.muslimummah"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val publisherFile = rootProject.file("publisher.md")
            val jksFile = rootProject.file("muslimummah.jks")

            if (publisherFile.exists() && jksFile.exists()) {
                val props = Properties()
                publisherFile.inputStream().use { inputStream ->
                    props.load(inputStream)
                }

                storeFile = jksFile
                storePassword = props.getProperty("storePassword") ?: "muslimummah123"
                keyAlias = props.getProperty("keyAlias") ?: "muslimummah"
                keyPassword = props.getProperty("keyPassword") ?: "muslimummah123"
            }
        }
    }

    buildTypes {
        getByName("release") {
            val releaseSigning = signingConfigs.findByName("release")
            if (releaseSigning != null && releaseSigning.storeFile?.exists() == true) {
                signingConfig = releaseSigning
            }
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.12.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
