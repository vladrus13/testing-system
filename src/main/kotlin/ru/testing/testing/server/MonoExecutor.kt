package ru.testing.testing.server

import ru.testing.database.ResultHolder
import ru.testing.testing.queue.TestingQueue
import ru.testing.utils.TestingConfiguration
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

/**
 * Mono thread executor. Take tasks and execute it
 *
 */
class MonoExecutor {

    private var isExited = false

    /**
     * Starts executor
     *
     */
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
            Files.walkFileTree(templateDirectory, object : FileVisitor<Path> {
                override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                    return FileVisitResult.CONTINUE
                }

                override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                    if (file != null) Files.delete(file)
                    return FileVisitResult.CONTINUE
                }

                override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult {
                    return FileVisitResult.CONTINUE
                }

                override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult {
                    if (dir != null) Files.delete(dir)
                    return FileVisitResult.CONTINUE
                }
            })
        }
    }

    /**
     * Try to stop executor (it wouldn't take any tasks anymore). It can take some time
     *
     */
    fun stop() {
        isExited = true
    }
}