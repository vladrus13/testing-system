package ru.testing.polygon.server

import EnvironmentConfiguration
import ru.testing.polygon.utils.TestingConfiguration
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

/**
 * Mono thread executor. Take tasks and executes it
 *
 */
class MonoExecutor {

    private var hasExited = false

    /**
     * Starts executor
     *
     */
    fun run(configuration: EnvironmentConfiguration) = with(configuration) {
        while (!hasExited) {
            val newTask = testingQueue.get() ?: continue
            val rootPath = TestingConfiguration.DEPLOY_DIRECTORY
            val temporaryDirectory = Files.createTempDirectory(rootPath, "temp")
            val solutionFile = temporaryDirectory.resolve(newTask.title)
            solutionFile.toFile().writeText(newTask.source)
            newTask.processFile.runSolverFile(configuration, newTask.id, solutionFile, newTask.task)
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