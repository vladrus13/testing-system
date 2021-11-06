plugins {
    kotlin("jvm")
    idea
}

group = "ru.testing"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":testlib"))
    implementation(kotlin("reflect"))
}