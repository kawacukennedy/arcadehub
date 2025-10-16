import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.plugins.JavaPluginExtension

plugins {
    kotlin("jvm") version "1.9.22" apply false
    id("org.openjfx.javafxplugin") version "0.1.0" 
    `kotlin-dsl`
}

allprojects {
    group = "com.arcadehub"
    version = "1.5.0"

    repositories {
        mavenCentral()
        google()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "java")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<KotlinCompile> {
        compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }

    dependencies {
        // Logging
        implementation("org.slf4j:slf4j-api:2.0.12")
        runtimeOnly("ch.qos.logback:logback-classic:1.5.3")

        // Testing
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
        testImplementation("org.mockito:mockito-core:5.11.0")
        testImplementation(kotlin("test"))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}