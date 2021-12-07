package ru.testing.polygon.server

import ru.testing.polygon.queue.TestingQueue
import ru.testing.polygon.utils.TestingConfiguration
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

/**
 * Mono thread executor. Takes tasks and executes it
 *
 */
class MonoExecutor {

    private var hasExited = false

    /**
     * Starts executor
     *
     */
    fun run() {
        while (!hasExited) {
            val newTask = TestingQueue.get() ?: continue
            val rootPath = TestingConfiguration.DEPLOY_DIRECTORY
            val temporaryDirectory = Files.createTempDirectory(rootPath, "temp")
            val solutionFile = temporaryDirectory.resolve(newTask.title)
            solutionFile.toFile().printWriter(StandardCharsets.UTF_8).use { out ->
                out.println(newTask.listing)
            } // todo: is it just writing to the file?
            // todo: why do you need to create a directory for the only file
            newTask.processFile.runSolverFile(newTask.id, solutionFile, newTask.task)
            Files.walkFileTree(temporaryDirectory, object : FileVisitor<Path> {
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
     * Tries to stop executor (it wouldn't take any tasks anymore). It can take some time
     *
     */
    fun stop() {
        hasExited = true
    }
}