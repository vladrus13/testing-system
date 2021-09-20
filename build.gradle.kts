import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
    application
}

group = "ru.testingsystem"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("ru.testingsystem.Launcherkt")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.21")

    implementation(project("ktor"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

rec(project, file("build").resolve("classes"))

fun rec(project: Project, buildDir: File) {
    project.buildDir = buildDir
    project.childProjects.forEach {
        rec(it.value, buildDir.resolve(it.key))
    }
}
