package ru.testing

import SimpleEnvironmentConfiguration
import interfaces.AbstractDatabaseInitializer
import kotlinx.coroutines.delay
import ru.testing.polygon.database.TypeOfLaunchingHolder
import ru.testing.polygon.queue.TestingQueue
import ru.testing.polygon.server.Executors
import ru.testing.polygon.submission.OlympiadCpp
import ru.testing.polygon.submission.OlympiadJava
import ru.testing.polygon.submission.OlympiadProgrammingLanguage
import ru.testing.polygon.submission.makeSubmission
import ru.testing.tasks.TasksHolder
import ru.testing.testlib.task.SubmissionVerdict
import ru.testing.testlib.task.SubmissionVerdict.*
import ru.testing.testlib.task.TestVerdict
import kotlin.time.ExperimentalTime

@ExperimentalTime
object TestUtils {
    enum class TestTask(val id: Long) {
        `A + B`(1L),
        `A + B + C`(2L),
    }

    suspend fun runTask(
        task: TestTask,
        language: OlympiadProgrammingLanguage,
        source: String,
    ): SubmissionVerdict {
        val configuration = SimpleEnvironmentConfiguration(
            TasksHolder(),
            TestingQueue(),
            Executors(1),
            InMemoryResultHolder(),
            InMemoryUserHolder(),
            object : AbstractDatabaseInitializer {
                override fun createSchema() = Unit
            },
            TypeOfLaunchingHolder()
        )
        val extension = when (language) {
            OlympiadCpp -> "cpp"
            OlympiadJava -> "java"
        }
        val submissionFile = makeSubmission(
            configuration,
            title = "Source.$extension", source = source, fileType = language,
            task = configuration.tasksHolder[task.id]!!,
            1
        )
        try {
            configuration.testingQueue.add(submissionFile)
            while (true) {
                when (val verdict = configuration.resultHolder.getVerdict(submissionFile.id)) {
                    is CompilationError, CompilationTimeLimit -> return verdict
                    is RunningVerdict -> if (TestVerdict.NL in verdict.tests) delay(1L) else return verdict
                    is NotLaunchedVerdict -> delay(1L)
                    null -> error("Submission not found")
                }
            }
        } finally {
            configuration.executors.stop()
        }
    }
}