package ru.testing.testing.submission

import ru.testing.database.ResultHolder
import ru.testing.testing.task.CompileRunTask
import ru.testing.testing.task.SubmissionVerdict
import ru.testing.testing.task.Task
import ru.testing.testing.task.TestVerdict
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

/**
 * Submission that compile and run
 *
 */
abstract class CompileRunSubmissionProcessFile : SubmissionProcessFile() {

    /**
     * Get compile command
     *
     * @param path path to file
     * @param name name of file
     * @return list of command
     */
    abstract fun getCompileCommand(path: Path, name: String): List<String>

    /**
     * Get run command
     *
     * @param name name of file
     * @return list of command
     */
    abstract fun getRunCommand(name: String): List<String>

    override fun runSolveFile(idSubmission: Long, path: Path, task: Task) {
        if (task !is CompileRunTask) throw IllegalStateException("Task must be compile and run task")
        val folder = path.parent
        val name = path.nameWithoutExtension
        val results = execute(
            command = getCompileCommand(path, name),
            directory = folder,
            limits = task.compile
        )
        if (results.code != 0) {
            ResultHolder.sendVerdict(idSubmission, SubmissionVerdict.CompilationError(results.error))
        } else {
            ResultHolder.sendVerdict(
                idSubmission, SubmissionVerdict.RunningVerdict(
                    ArrayList(task.tests.indices.map { TestVerdict.NL() })
                )
            )
            task.tests.forEachIndexed { index, test ->
                val execution = execute(
                    command = getRunCommand(name),
                    directory = folder,
                    input = test.input,
                    limits = task.run
                )
                ResultHolder.sendVerdict(
                    idSubmission, index,
                    if (execution.code != 0) {
                        TestVerdict.RE(execution.code, execution.error)
                    } else {
                        test.verdict(execution.output)
                    }
                )
            }
        }
    }
}