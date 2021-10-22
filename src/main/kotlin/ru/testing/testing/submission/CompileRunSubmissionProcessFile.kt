package ru.testing.testing.submission

import ru.testing.testing.task.CompileRunTask
import ru.testing.testing.task.Task
import ru.testing.testing.task.TestVerdict
import java.nio.file.Path
import kotlin.io.path.nameWithoutExtension

abstract class CompileRunSubmissionProcessFile : SubmissionProcessFile() {

    abstract fun getCompileCommand(path: Path, name: String): List<String>

    abstract fun getRunCommand(name: String): List<String>

    override fun runSolveFile(path: Path, task: Task): List<TestVerdict> {
        if (task !is CompileRunTask) throw IllegalStateException("Task must be compile and run task")
        val folder = path.parent
        val name = path.nameWithoutExtension
        val results = execute(
            command = getCompileCommand(path, name),
            directory = folder,
            limits = task.compile
        )
        if (results.code != 0) {
            return mutableListOf(TestVerdict.CE(results.error))
        }
        val result: MutableList<TestVerdict> = mutableListOf()
        task.tests.forEach {
            val execution = execute(
                command = getRunCommand(name),
                directory = folder,
                input = it.input,
                limits = task.run
            )
            result.add(
                if (execution.code != 0) {
                    TestVerdict.RE(execution.code, execution.error)
                } else {
                    it.verdict(execution.output)
                }
            )
        }
        return result
    }
}