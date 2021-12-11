plugins {
    application
    kotlin("jvm") version "1.5.31"
}

// TODO make children group and version dependencies

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
    implementation("io.ktor:ktor-server-core:1.6.6")
    implementation("io.ktor:ktor-server-netty:1.6.6")
    implementation("ch.qos.logback:logback-classic:1.2.7")
    implementation(project("html-server"))
    implementation(project("polygon"))
    implementation(project("configuration"))
    implementation(project("tasks"))
    implementation(project("testlib"))
    implementation(project("model"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}