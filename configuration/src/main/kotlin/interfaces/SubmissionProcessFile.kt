package interfaces

import EnvironmentConfiguration
import ru.testing.testlib.limits.Limits
import ru.testing.testlib.task.Task
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.concurrent.TimeUnit

/**
 * Submission type. Runs solution we get
 *
 */
abstract class SubmissionProcessFile {

    /**
     * Output of execution
     *
     * @property output output of program
     * @property error error stream of program
     * @property code exiting code of program
     */
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

    /**
     * Execute command
     *
     * @param command command what we execution
     * @param directory directory where we're executing
     * @param input input which we give to execution
     * @param limits limits of execution
     * @return
     */
    fun execute(
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

    /**
     * Runa solver file on tests
     *
     * @param path path to solver
     * @param task task to test
     * @return list of received verdicts
     */
    abstract fun runSolverFile(configuration: EnvironmentConfiguration, idSubmission: Long, path: Path, task: Task)
}