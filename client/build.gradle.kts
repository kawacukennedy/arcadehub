plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

application {
    mainClass.set("com.arcadehub.client.MainApp")
}

javafx {
    version = "17.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":shared"))
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:1.12.1")
    implementation("com.badlogicgames.gdx:gdx:1.12.1")
    implementation("io.netty:netty-all:4.1.100.Final")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
}
