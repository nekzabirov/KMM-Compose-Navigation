pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenLocal()
    }

    plugins {
        kotlin("multiplatform").version("1.9.0")
        kotlin("plugin.serialization").version("1.9.0")
        id("com.android.library").version("8.1.0-rc01")
        id("org.jetbrains.compose").version("1.5.2")
    }
}

rootProject.name = "navigation"