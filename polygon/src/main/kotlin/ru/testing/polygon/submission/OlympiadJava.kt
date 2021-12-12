package ru.testing.polygon.submission

import java.nio.file.Path
import kotlin.io.path.absolutePathString

/**
 * Java implementation of SubmissionProcessFIle
 *
 */
object OlympiadJava : OlympiadProgrammingLanguage() {
    override fun getName(): String = "Java 11"

    override fun getCodeName(): String = "java11"

    override fun getCompilingCommand(path: Path, name: String): List<String> =
        listOf("javac", path.absolutePathString())

    override fun getRunningCommand(name: String): List<String> = listOf("java", "-cp", ".", name)
}