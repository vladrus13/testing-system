plugins {
    application
    kotlin("jvm") version "1.5.31"
}

// TODO make childs group and version dependencies

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
    implementation(project("html-server"))
    implementation(project("polygon"))
}