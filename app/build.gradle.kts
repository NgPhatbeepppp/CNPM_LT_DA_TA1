plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.cnpm_lt_da_ta"
    compileSdk = 34



    defaultConfig {
        applicationId = "com.example.cnpm_lt_da_ta"
        minSdk = 27
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
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")  // 1.12.0 -> 1.9.0
    implementation("com.google.firebase:firebase-auth:22.1.1")  // 23.0.0 -> 22.1.1

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")      // 1.1.0 -> 1.0.0
    implementation("androidx.recyclerview:recyclerview:1.2.1")   // 1.3.2 -> 1.2.1
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(platform("com.google.firebase:firebase-bom:32.2.0")) // 33.1.0 -> 32.2.0
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage")

    //
    implementation("de.hdodenhof:circleimageview:3.1.0")
    //
    implementation("com.github.bumptech.glide:glide:4.15.1")     // 4.16.0 -> 4.15.1

    implementation("com.squareup.picasso:picasso:2.71828")
}

