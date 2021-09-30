package ru.testing.testing.server

import ru.testing.database.ResultHolder
import ru.testing.testing.queue.TestingQueue
import ru.testing.utils.TestingConfiguration
import java.nio.charset.StandardCharsets
import java.nio.file.Files

class MonoExecutor {

    private var isExited = false

    fun run() {
        while (!isExited) {
            val newTask = TestingQueue.get() ?: continue
            val rootPath = TestingConfiguration.DEPLOY_DIRECTORY
            val templateDirectory = Files.createTempDirectory(rootPath, "temp")
            val solveFile = templateDirectory.resolve("${newTask.title}")
            solveFile.toFile().printWriter(StandardCharsets.UTF_8).use { out ->
                out.println(newTask.listing)
            }
            val results = newTask.fileType.runSolveFile(solveFile, newTask.task)
            ResultHolder.holder[ResultHolder.holder.size.toLong()] = results
        }
    }

    fun stop() {
        isExited = true
    }
}