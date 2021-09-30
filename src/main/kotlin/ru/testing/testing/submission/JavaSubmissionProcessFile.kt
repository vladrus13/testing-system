package ru.testing.testing.submission

import ru.testing.testing.limits.Limits
import ru.testing.testing.task.Task
import ru.testing.testing.task.TestVerdict
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.nameWithoutExtension

/**
 * Java implementation of SubmissionProcessFIle
 *
 */
class JavaSubmissionProcessFile : SubmissionProcessFile() {
    override fun runSolveFile(path: Path, task: Task): List<TestVerdict> {
        val folder = path.parent
        val name = path.nameWithoutExtension
        // TODO results exit code != 0
        val results = execute(
            command = listOf("javac", path.absolutePathString()),
            directory = folder,
            limits = Limits.COMPILATION_LIMITS
        )
        val result: MutableList<TestVerdict> = mutableListOf()
        task.tests.forEach {
            // TODO not ignore wrong execution
            result.add(
                it.verdict(
                    execute(
                        command = listOf("java", "-cp", ".", name),
                        directory = folder,
                        input = it.input,
                        limits = Limits.OLYMPIC_LIMITS
                    ).output
                )
            )
        }
        return result
    }
}