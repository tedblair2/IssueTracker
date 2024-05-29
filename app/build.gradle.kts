plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.apollo.plugin)
    alias(libs.plugins.google.services.plugin)
    alias(libs.plugins.parcelable)
    alias(libs.plugins.protobuf.plugin)
}

apollo {
    service("service") {
        packageName.set("com.github.tedblair2")
    }
}

android {
    namespace = "com.github.tedblair2.issuetracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.github.tedblair2.issuetracker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.github.tedblair2.issuetracker.CustomRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.extension.version.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions.unitTests {
        isReturnDefaultValues = true
        all { tests->
            tests.useJUnitPlatform()
            tests.testLogging {
                events("passed","failed","skipped")
            }
        }
    }
}

dependencies {
    implementation(libs.bundles.androidx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.hilt)
    ksp(libs.dagger.hilt.ksp)
    implementation(libs.apollo)
    implementation(libs.bundles.kotlinx)
    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    //datastore
    implementation(libs.bundles.datastore)
    //paging
    implementation(libs.bundles.paging)
    //coil
    implementation(libs.coil)

    androidTestImplementation(libs.hilt.test)
    kspAndroidTest(libs.hilt.test.ksp)
    testRuntimeOnly(libs.junit.jupiter.engine)
    androidTestImplementation(libs.truth)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
protobuf{
    protoc {
        artifact = "com.google.protobuf:protoc:3.9.1"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}

