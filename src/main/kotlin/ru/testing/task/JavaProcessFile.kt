package ru.testing.task

import ru.testing.limits.Limits
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.nameWithoutExtension

class JavaProcessFile : ProcessFile() {
    override fun runSolveFile(path: Path, input: String): OutputProcessFile {
        val folder = path.parent
        val name = path.nameWithoutExtension
        // TODO results exit code != 0
        val results = getProcessBuilder(
            command = listOf("javac", path.absolutePathString()),
            directory = folder,
            limits = Limits.COMPILATION_LIMITS
        )
        if (results.code != 0) {
            return results
        }
        val classFile = folder.resolve(name)
        return getProcessBuilder(
            command = listOf("java", "-cp", ".", name),
            directory = folder,
            input = input,
            limits = Limits.OLYMPIC_LIMITS
        )
    }
}