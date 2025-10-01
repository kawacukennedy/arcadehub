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
    applicationDefaultJvmArgs = listOf(
        "--module-path", layout.buildDirectory.dir("install/client/lib").get().asFile.absolutePath,
        "--add-modules", "javafx.controls,javafx.fxml,javafx.media",
        "-Djava.library.path=" + layout.buildDirectory.dir("install/client/lib").get().asFile.absolutePath
    )
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.arcadehub.client.Main"
    }
}

dependencies {
    // Common module
    implementation(project(":common"))

    // Networking
    implementation("io.netty:netty-all:4.1.108.Final")

    // Game Rendering
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion") {
        exclude(group = "org.lwjgl") // Exclude all LWJGL dependencies from gdx-backend-lwjgl3
    }

    // Explicit LWJGL native dependencies for macOS ARM64
    runtimeOnly("org.lwjgl:lwjgl:3.3.3:natives-macos-arm64")
    runtimeOnly("org.lwjgl:lwjgl-glfw:3.3.3:natives-macos-arm64")
    runtimeOnly("org.lwjgl:lwjgl-opengl:3.3.3:natives-macos-arm64")
    runtimeOnly("org.lwjgl:lwjgl-jemalloc:3.3.3:natives-macos-arm64")
    runtimeOnly("org.lwjgl:lwjgl-stb:3.3.3:natives-macos-arm64")
    runtimeOnly("org.lwjgl:lwjgl-tinyfd:3.3.3:natives-macos-arm64")

    // JSON Serialization
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
}