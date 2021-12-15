plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.31"
    idea
}

group = "ru.testing"
version = "0.0.1"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    implementation("io.ktor:ktor-html-builder:1.6.4")
}