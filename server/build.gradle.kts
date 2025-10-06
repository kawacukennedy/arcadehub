plugins {
    application
}

application {
    mainClass.set("com.arcadehub.server.ServerMain")
}

tasks.register<JavaExec>("runServer") {
    group = "run"
    classpath = files(sourceSets["main"].runtimeClasspath, configurations.runtimeClasspath)
    mainClass.set("com.arcadehub.server.ServerMain")
}

dependencies {
    // Common module
    implementation(project(":common"))

    // Networking
    implementation("io.netty:netty-all:4.1.108.Final")

    // Database
    implementation("org.hibernate.orm:hibernate-core:6.5.0.Final")
    implementation("org.hibernate.orm:hibernate-community-dialects:6.5.0.Final") // For SQLiteDialect
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("org.xerial:sqlite-jdbc:3.45.2.0")
    implementation("com.zaxxer:HikariCP:5.1.0")

    // JSON Serialization
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")

    // JAXB for persistence.xml parsing
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    runtimeOnly("org.glassfish.jaxb:jaxb-runtime:4.0.0")
}