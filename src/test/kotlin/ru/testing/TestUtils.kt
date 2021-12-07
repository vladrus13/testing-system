package ru.testing

import SimpleEnvironmentConfiguration
import ru.testing.polygon.submission.ProgrammingLanguage
import kotlinx.coroutines.delay
import ru.testing.polygon.database.ResultHolder
import ru.testing.polygon.queue.TestingQueue
import ru.testing.polygon.server.Executors
import ru.testing.polygon.submission.Cpp
import ru.testing.polygon.submission.Java
import ru.testing.polygon.submission.makeSubmission
import ru.testing.tasks.TasksHolder
import ru.testing.testlib.limits.Limits
import ru.testing.testlib.task.SubmissionVerdict
import ru.testing.testlib.task.SubmissionVerdict.*
import ru.testing.testlib.task.TestVerdict
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
object TestUtils {
    enum class TestTask(val id: Long) {
        `A + B`(1L),
        `A + B + C`(2L),
    }

    suspend fun runTask(
        task: TestTask,
        language: ProgrammingLanguage,
        source: String,
    ): SubmissionVerdict {
        val configuration = SimpleEnvironmentConfiguration(TasksHolder(), TestingQueue(), Executors(1), ResultHolder())
        val extension = when (language) {
            Cpp -> "cpp"
            Java -> "java"
        }
        val submissionFile = makeSubmission(
            configuration,
            title = "Source.$extension", source = source, fileType = language,
            task = configuration.tasksHolder[task.id]!!
        )
        try {
            configuration.testingQueue.add(submissionFile)
            val startTime = Instant.now()
            while (true) {
                when (val verdict = configuration.resultHolder.getVerdict(submissionFile.id)) {
                    is CompilationError -> return verdict
                    is RunningVerdict -> if (TestVerdict.NL in verdict.tests) delay(1L) else return verdict
                    is NotLaunchedVerdict -> {
                        val tl = Limits.COMPILATION_LIMITS.timeLimitMilliseconds
                        if (Instant.now().toEpochMilli() - startTime.toEpochMilli() <= tl) delay(1L)
                        else error("TL: $submissionFile compilation has exceeded $tl")
                    }
                    null -> error("Submission not found")
                }
            }
        } finally {
            configuration.executors.stop()
        }
    }
}