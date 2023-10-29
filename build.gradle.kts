group = "com.nekzabirov"
version = "1.0.4"

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("maven-publish")
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    mavenLocal()
}

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    androidTarget {
        publishAllLibraryVariants()
    }

    jvm("desktop")

    ios()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries {
            framework {
                baseName = "common"
                isStatic = true
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.ui)
                api(compose.foundation)
                api(compose.materialIconsExtended)
                api(compose.material3)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.12.0")
                api("androidx.activity:activity-compose:1.8.0")
            }
        }

        val iosMain by getting
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
    }

    explicitApi()

    jvmToolchain(17)
}

android {
    namespace = "com.nekzabirov.navigatio.common"

    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components.getByName("kotlin"))
            groupId = group.toString()
            artifactId = "navigation"
            version = project.version.toString()
        }
    }

    repositories {
        mavenLocal()

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/nekzabirov/KMM-Compose-Navigation")

            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_FULL_TOKEN")
            }
        }
    }
}
