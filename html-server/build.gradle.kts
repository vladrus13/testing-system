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
    implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.251-kotlin-1.5.31")
    implementation(project(":polygon"))
    implementation(project(":testlib"))
    implementation(project(":tasks"))
    implementation(project(":configuration"))
}