import org.openjfx.gradle.JavaFXOptions

plugins {
    id("org.openjfx.javafxplugin")
    application
}

val gdxVersion = "1.12.1"

javafx {
    version = "20"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.media")
}

application {
    mainClass.set("com.arcadehub.client.Main")
}

dependencies {
    // Common module
    implementation(project(":common"))

    // Networking
    implementation("io.netty:netty-all:4.1.108.Final")

    // Game Rendering
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    runtimeOnly("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")


    // JSON Serialization
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
}
