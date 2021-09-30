package ru.testing.testing.submission

import ru.testing.testing.limits.Limits
import ru.testing.testing.task.Task
import ru.testing.testing.task.TestVerdict
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.nameWithoutExtension

class CPPSubmissionProcessFile : SubmissionProcessFile() {
    override fun runSolveFile(path: Path, task: Task): List<TestVerdict> {
        val folder = path.parent
        val name = path.nameWithoutExtension
        // TODO results exit code != 0
        val results = getProcessBuilder(
            command = listOf("g++", path.absolutePathString(), "-o", name),
            directory = folder,
            limits = Limits.COMPILATION_LIMITS
        )
        val result: MutableList<TestVerdict> = mutableListOf()
        task.tests.forEach {
            // TODO not ignore wrong execution
            result.add(
                it.verdict(
                    // TODO not only linux execution
                    getProcessBuilder(
                        command = listOf("./${name}"),
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