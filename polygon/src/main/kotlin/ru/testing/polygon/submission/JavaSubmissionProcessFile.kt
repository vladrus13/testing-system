package ru.testing.polygon.submission

import java.nio.file.Path
import kotlin.io.path.absolutePathString

/**
 * Java implementation of SubmissionProcessFIle
 *
 */
class JavaSubmissionProcessFile : CompileRunSubmissionProcessFile() {
    override fun getCompileCommand(path: Path, name: String): List<String> = listOf("javac", path.absolutePathString())

    override fun getRunCommand(name: String): List<String> = listOf("java", "-cp", ".", name)
}