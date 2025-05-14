
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

// Function to safely load properties from local.properties
fun getApiKey(project: Project, propertyName: String): String {
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        val properties = Properties()
        FileInputStream(localPropertiesFile).use { fis ->
            properties.load(fis)
        }
        // Return property value or an empty string if not found
        // The quotes are expected to be part of the value in local.properties
        return properties.getProperty(propertyName, "\"\"")
    }
    // Fallback for CI: Read from environment variable if local.properties doesn't exist or key missing
    // Gradle automatically makes environment variables available as project properties
    // Note: env var names often match property names, but can be different if mapped in CI
    return project.findProperty(propertyName)?.toString() ?: "\"\"" // Default to empty string literal if not found anywhere
}


android {
    namespace = "com.eltonkola.stox"
    compileSdk = 35
    buildToolsVersion = "34.0.0"

    defaultConfig {
        applicationId = "com.eltonkola.stox"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val polygonApiKey = getApiKey(project, "polygonApiKey")
        buildConfigField("String", "PolygonApiKey", "\"$polygonApiKey\"")

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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.bundles.networking)
    implementation(libs.bundles.coroutines)

    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.compose)



    implementation(project(":app:core-ui"))
    implementation(project(":app:core-data"))
    implementation(project(":app:core-domain"))

    implementation(project(":app:feature-add-stock"))
    implementation(project(":app:feature-stock-list"))
    implementation(project(":app:feature-stock-detail"))
    implementation(project(":app:feature-stocks-overview"))


    implementation(libs.bundles.hilt)
    kapt(libs.hilt.compiler)

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}