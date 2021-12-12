package ru.testing.polygon.submission

import EnvironmentConfiguration
import ru.testing.polygon.queue.SubmissionProcessFile
import ru.testing.testlib.task.CompileRunTask
import ru.testing.testlib.task.SubmissionVerdict
import ru.testing.testlib.task.SubmissionVerdict.RunningVerdict
import ru.testing.testlib.task.Task
import ru.testing.testlib.task.TestVerdict.*
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

/**
 * Submission to compile and run
 *
 */
sealed class ProgrammingLanguage : SubmissionProcessFile() {

    /**
     * Gets compiling command
     *
     * @param path path to the file
     * @param name name of the file
     * @return list of commands
     */
    abstract fun getCompilingCommand(path: Path, name: String): List<String>

    /**
     * Gets running command
     *
     * @param name name of the file
     * @return list of the commands
     */
    abstract fun getRunningCommand(name: String): List<String>

    override fun runSolverFile(
        configuration: EnvironmentConfiguration, submissionId: Long, path: Path, task: Task
    ) = with(configuration) {
        when (task) {
            is CompileRunTask -> {
                val folder = path.parent
                val name = path.nameWithoutExtension
                val results = execute(
                    command = getCompilingCommand(path, name),
                    directory = folder,
                    limits = task.compile
                ) ?: return@with resultHolder.sendVerdict(submissionId, SubmissionVerdict.CompilationTimeLimit)
                if (results.code != 0) {
                    resultHolder.sendVerdict(submissionId, SubmissionVerdict.CompilationError(results.error))
                } else {
                    resultHolder.sendVerdict(submissionId, RunningVerdict(ArrayList(task.textTests.indices.map { NL })))
                    task.textTests.forEachIndexed { index, test ->
                        val execution = execute(
                            command = getRunningCommand(name),
                            directory = folder,
                            input = test.getInput(),
                            limits = task.run
                        )
                        resultHolder.sendVerdict(
                            submissionId, index,
                            when {
                                execution == null -> TL
                                execution.code != 0 -> RE(execution.code, execution.error)
                                else -> test.verdict(execution.output)
                            }
                        )
                    }
                }
            }
        }
    }
}