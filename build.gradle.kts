plugins {
    application
    kotlin("jvm") version "1.5.31"
}

group = "ru.testing"
version = "0.0.1"
application {
    mainClass.set("ru.testing.ApplicationKt")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

dependencies {
    implementation("io.ktor:ktor-server-core:1.6.4")
    implementation("io.ktor:ktor-server-netty:1.6.4")
    implementation("ch.qos.logback:logback-classic:1.2.6")
    testImplementation("io.ktor:ktor-server-tests:1.6.4")
    implementation("io.ktor:ktor-html-builder:1.6.4")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.251-kotlin-1.5.31")
    implementation("com.github.docker-java:docker-java:3.2.12")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.0-M1")
}