plugins {
    kotlin("jvm")
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":server")) // For replay player

    // CLI parsing
    implementation("com.github.ajalt.clikt:clikt:4.2.2")

    // For replay inspector/player
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("org.apache.commons:commons-compress:1.26.0") // For gzip
}

application {
    mainClass.set("com.arcadehub.tools.ReplayInspector")
}