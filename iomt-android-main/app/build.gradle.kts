

plugins {
    kotlin("plugin.serialization") version "1.8.10"
    id("com.android.application")
    kotlin("android")
    id("org.cqfn.diktat.diktat-gradle-plugin")
    id("com.google.devtools.ksp")
    id("org.jetbrains.dokka")
}

android {
    buildFeatures {
        compose = true
    }
    compileSdk = 33
    compileSdkPreview = "UpsideDownCake"
    defaultConfig {
        multiDexEnabled = true
        applicationId = "com.iomt.android"
        targetSdk = 33
        minSdk = 23
        versionCode = 2
        versionName = "1.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro",
            )
            multiDexEnabled = true
        }
        getByName("debug") {
            multiDexEnabled = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    buildToolsVersion = "33.0.2"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    dependenciesInfo {
        includeInApk = true
        includeInBundle = true
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packagingOptions {
        resources {
            excludes.addAll(listOf("META-INF/INDEX.LIST", "META-INF/io.netty.versions.properties"))
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.21")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    implementation("androidx.activity:activity-compose:1.7.1")
    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.runtime:runtime:1.4.3")
    implementation("androidx.compose.compiler:compiler:1.4.7")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation("androidx.bluetooth:bluetooth:1.0.0-alpha02")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.3")

    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.5.3")

    implementation("androidx.room:room-runtime:2.5.1")
    implementation("androidx.room:room-ktx:2.5.1")
    implementation("androidx.room:room-paging:2.5.1")
    ksp("androidx.room:room-compiler:2.5.1")

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.credentials:credentials:1.2.0-alpha03")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.0-alpha03")

    implementation("com.google.android.material:material:1.9.0")

    implementation("com.hivemq:hivemq-mqtt-client:1.3.0")

    implementation("com.patrykandpatrick.vico:core:1.6.5")
    implementation("com.patrykandpatrick.vico:compose:1.6.5")
    implementation("com.patrykandpatrick.vico:compose-m3:1.6.5")

    implementation("io.ktor:ktor-client-core:2.2.4")
    implementation("io.ktor:ktor-client-auth:2.2.4")
    implementation("io.ktor:ktor-client-android:2.2.4")
    implementation("io.ktor:ktor-client-logging:2.2.4")
    implementation("io.ktor:ktor-client-logging-jvm:2.2.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.4")

    implementation("no.nordicsemi.android:ble:2.6.1")
    implementation("no.nordicsemi.android:ble-ktx:2.6.1")
    implementation("no.nordicsemi.android:ble-common:2.6.1")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("io.ktor:ktor-client-mock:2.2.4")
    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    dokkaPlugin("org.jetbrains.dokka:android-documentation-plugin:1.8.10")
}

configurations.all {
    resolutionStrategy { force("androidx.core:core-ktx:1.6.0") }
}

diktat {
    diktatConfigFile = rootProject.file("diktat-analysis.yml")
    githubActions = findProperty("diktat.githubActions")?.toString()?.toBoolean() ?: false
    inputs {
        include("src/**/*.kt", "*.kts", "src/**/*.kts")
        exclude("build/**", "src/test/**", "src/androidTest/**")
    }
}
