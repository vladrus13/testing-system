package ru.testing.polygon.queue

import interfaces.AbstractSubmissionProcessFile
import ru.testing.testlib.limits.Limits
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.time.Instant
import java.util.concurrent.TimeUnit

/**
 * Submission type. Runs solution we get
 *
 */
abstract class SubmissionProcessFile : AbstractSubmissionProcessFile {

    private fun setInput(path: Path, input: String) {
        path.toFile().printWriter(StandardCharsets.UTF_8).use {
            it.println(input)
        }
    }

    private fun getOutput(path: Path): String =
        path.toFile().bufferedReader(StandardCharsets.UTF_8).use {
            it.readText()
        }

    override fun execute(
        command: List<String>,
        directory: Path,
        input: String?,
        limits: Limits
    ): AbstractSubmissionProcessFile.OutputProcessFile? {
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
        val startTime = Instant.now().toEpochMilli()
        process.waitFor(limits.timeLimitMilliseconds * 3 / 2, TimeUnit.MILLISECONDS)
        val exitValue = try {
            process.exitValue()
        } catch (e: IllegalThreadStateException) {
            if (e.message == "process hasn't exited") {
                process.destroyForcibly()
                return null
            }
            throw e
        }
        val elapsed = Instant.now().toEpochMilli() - startTime
        if (elapsed > limits.timeLimitMilliseconds) {
            return null
        }
        if (exitValue != 0) {
            val error = process.errorStream.use {
                it.bufferedReader().use { it1 ->
                    it1.readText()
                }
            }
            return AbstractSubmissionProcessFile.OutputProcessFile(
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
            return AbstractSubmissionProcessFile.OutputProcessFile(
                output = output,
                error = error,
                code = exitValue
            )
        }
    }
}