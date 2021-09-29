package ru.testing.testing.submission

import ru.testing.testing.limits.Limits
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.concurrent.TimeUnit

abstract class SubmissionProcessFile {

    class OutputProcessFile(
        val output: String = "",
        val error: String = "",
        val code: Int = 0
    )

    private fun setInput(path: Path, input: String) {
        path.toFile().printWriter(StandardCharsets.UTF_8).use {
            it.println(input)
        }
    }

    private fun getOutput(path: Path): String =
        path.toFile().bufferedReader(StandardCharsets.UTF_8).use {
            it.readText()
        }

    fun getProcessBuilder(
        command: List<String>,
        directory: Path,
        input: String? = null,
        limits: Limits
    ): OutputProcessFile {
        val processBuilder = ProcessBuilder(command)
        processBuilder.directory(directory.toFile())
        if (input != null) {
            val inputPath = directory.resolve("input.txt")
            setInput(inputPath, input)
            processBuilder.redirectInput(inputPath.toFile())
        }
        val outputFile = directory.resolve("output.txt")
        processBuilder.redirectOutput(outputFile.toFile())
        val process = processBuilder.start()
        process.waitFor(limits.timeLimitMilliseconds * 3 / 2, TimeUnit.MILLISECONDS)
        val exitValue = process.exitValue()
        if (exitValue != 0) {
            val error = process.errorStream.use {
                it.bufferedReader().use { it1 ->
                    it1.readText()
                }
            }
            return OutputProcessFile(
                error = error,
                code = exitValue
            )
        } else {
            val error = process.errorStream.use {
                it.bufferedReader().use { it1 ->
                    it1.readText()
                }
            }
            val output = getOutput(outputFile)
            return OutputProcessFile(
                output = output,
                error = error,
                code = exitValue
            )
        }
    }

    abstract fun runSolveFile(path: Path, input: String): OutputProcessFile
}