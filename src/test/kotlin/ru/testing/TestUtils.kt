package ru.testing

import SimpleEnvironmentConfiguration
import ru.testing.polygon.submission.ProgrammingLanguage
import interfaces.SubmissionFile
import kotlinx.coroutines.delay
import ru.testing.polygon.database.ResultHolder
import ru.testing.polygon.queue.TestingQueue
import ru.testing.polygon.server.Executors
import ru.testing.polygon.submission.makeSubmission
import ru.testing.tasks.TasksHolder
import ru.testing.testlib.task.SubmissionVerdict
import ru.testing.testlib.task.SubmissionVerdict.*
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
object TestUtils {
    enum class TestTask(val id: Long) {
        `A + B`(1L),
        `A + B + C`(2L),
    }

    suspend fun runTask(task: TestTask, language: ProgrammingLanguage, source: String, tl: Duration = Duration.seconds(5)): SubmissionVerdict {
        val configuration = SimpleEnvironmentConfiguration(TasksHolder(), TestingQueue(), Executors(1), ResultHolder())
        val submissionFile = makeSubmission(configuration,
            title = "TestSource", source = source, fileType = language,
            task = configuration.tasksHolder[task.id]!!
        )
        try {
            configuration.testingQueue.add(submissionFile)
        } finally {
            configuration.executors.stop()
        }
        val startTime = Instant.now()
        while (true) {
            when(val verdict = configuration.resultHolder.getVerdict(submissionFile.id)) {
                is CompilationError, is RunningVerdict -> return verdict
                is NotLaunchedVerdict ->
                    if (Instant.now().toEpochMilli() - startTime.toEpochMilli() <= tl.inWholeMilliseconds) delay(1L)
                    else error("TL: $submissionFile has exceeded $tl")
                null -> error("Submission not found")
            }
        }
    }
}