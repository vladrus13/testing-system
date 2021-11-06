plugins {
    kotlin("jvm")
    idea
}

group = "ru.testing"
version = "0.0.1"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

dependencies {
    implementation("io.ktor:ktor-html-builder:1.6.4")
}