package ru.testing.polygon.submission

import java.nio.file.Path
import kotlin.io.path.absolutePathString

/**
 * C++ implementation of SubmissionProcessFIle
 *
 */
object Cpp : ProgrammingLanguage() {
    override fun getCompilingCommand(path: Path, name: String): List<String> =
        listOf("g++", path.absolutePathString(), "-v", "-o", name)

    override fun getRunningCommand(name: String): List<String> = listOf("./${name}")
}