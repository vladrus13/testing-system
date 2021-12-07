package ru.testing.polygon.submission

import ru.testing.polygon.database.ResultHolder.Companion.sendVerdict
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
abstract class CompileRunSubmissionProcessFile : SubmissionProcessFile() {

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

    override fun runSolverFile(idSubmission: Long, path: Path, task: Task) {
        if (task !is CompileRunTask) error("Task must be compiled and run")
        val folder = path.parent
        val name = path.nameWithoutExtension
        val results = execute(
            command = getCompilingCommand(path, name),
            directory = folder,
            limits = task.compile
        )
        if (results.code != 0) {
            sendVerdict(idSubmission, SubmissionVerdict.CompilationError(results.error))
        } else {
            sendVerdict(idSubmission, RunningVerdict(ArrayList(task.tests.indices.map { NL })))
            task.tests.forEachIndexed { index, test ->
                val execution = execute(
                    command = getRunningCommand(name),
                    directory = folder,
                    input = test.input,
                    limits = task.run
                )
                sendVerdict(
                    idSubmission, index,
                    if (execution.code != 0) RE(execution.code, execution.error) else test.verdict(execution.output)
                )
            }
        }
    }
}