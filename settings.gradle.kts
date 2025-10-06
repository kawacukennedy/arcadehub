
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
    plugins {
        id("org.openjfx.javafxplugin") version "0.1.0"
    }
}

rootProject.name = "ArcadeHub"
include("client", "server", "common")
