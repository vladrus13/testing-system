package ru.testing.polygon.submission

import java.nio.file.Path
import kotlin.io.path.absolutePathString

/**
 * C++ implementation of SubmissionProcessFIle
 *
 */
class CPPSubmissionProcessFile : CompileRunSubmissionProcessFile() {
    override fun getCompilingCommand(path: Path, name: String): List<String> =
        listOf("g++", path.absolutePathString(), "-o", name)

    override fun getRunningCommand(name: String): List<String> = listOf("./${name}")
}