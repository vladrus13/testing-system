package ru.testing.polygon.submission

import java.nio.file.Path
import kotlin.io.path.absolutePathString

/**
 * C++ implementation of SubmissionProcessFIle
 *
 */
object OlympiadCpp : OlympiadProgrammingLanguage() {
    override fun getName(): String = "C++17"

    override fun getCodeName(): String = "cpp17"

    override fun getCompilingCommand(path: Path, name: String): List<String> =
        listOf("g++", "-std=c++17", path.absolutePathString(), "-v", "-o", name)

    override fun getRunningCommand(name: String): List<String> = listOf("./${name}")
}
