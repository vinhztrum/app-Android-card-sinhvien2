plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.ute.studentprofileacademicmanager"

    // 1. Sửa lại cú pháp compileSdk (thường là 34 hoặc 35)
    compileSdk = 37

    // 2. Di chuyển buildFeatures ra ngoài, nằm ngang hàng với compileSdk và defaultConfig
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.ute.studentprofileacademicmanager"
        minSdk = 24
        // 3. Nên đồng bộ targetSdk với compileSdk
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Lưu ý: Cú pháp chuẩn của Android Gradle để tắt tối ưu hóa thường là:
            // isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}