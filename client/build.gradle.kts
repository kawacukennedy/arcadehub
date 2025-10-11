import org.openjfx.gradle.JavaFXOptions

plugins {
    id("org.openjfx.javafxplugin")
    application
}

val gdxVersion = "1.12.1"

javafx {
    version = "17.0.2"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.media")
}

application {
    mainClass.set("com.arcadehub.client.MainApp")
    applicationDefaultJvmArgs = listOf(
        "--module-path", layout.buildDirectory.dir("install/client/lib").get().asFile.absolutePath,
        "--add-modules", "javafx.controls,javafx.fxml,javafx.media",
        "-Djava.library.path=" + layout.buildDirectory.dir("install/client/lib").get().asFile.absolutePath
    )
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.arcadehub.client.MainApp"
    }
}

tasks.register<JavaExec>("jpackageLinux") {
    group = "distribution"
    description = "Package client for Linux using jpackage"
    classpath = files(sourceSets["main"].runtimeClasspath, configurations.runtimeClasspath)
    mainClass.set("jdk.jpackage.main.Main")
    args = listOf(
        "--type", "deb",
        "--name", "ArcadeHub",
        "--input", "build/libs",
        "--main-jar", "client.jar",
        "--main-class", "com.arcadehub.client.MainApp",
        "--app-version", "1.5.0",
        "--linux-shortcut"
    )
}

dependencies {
    // Shared module
    implementation(project(":shared"))

    // Networking
    implementation("io.netty:netty-all:4.1.108.Final")

    // Game Rendering
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")

    // Reverting to natives-desktop and will manually remove unwanted natives
    runtimeOnly("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")

    // JSON Serialization
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
}
