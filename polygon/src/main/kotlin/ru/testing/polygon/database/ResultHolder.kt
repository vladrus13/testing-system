package ru.testing.polygon.database

import ru.testing.testlib.task.SubmissionVerdict
import ru.testing.testlib.task.Task
import ru.testing.testlib.task.TestVerdict
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Result holder. Temporary solution. Will be replaced by a database
 *
 */
class ResultHolder {
    companion object {
        /**
         * Holds all results by their ids
         */
        private val holder: ConcurrentMap<Long, SubmissionVerdict> = ConcurrentHashMap()

        private val futureId: AtomicLong = AtomicLong(0)

        /**
         * Gets a verdict of submission
         *
         * @param submissionId id of submission
         * @return verdict
         */
        fun getVerdict(submissionId: Long) = holder[submissionId]

        /**
         * Adds a task to holder
         *
         * @param task task
         * @return id of the submission
         */
        fun addTask(task: Task): Long {
            val id = futureId.incrementAndGet()
            holder[id] = SubmissionVerdict.NotLaunchedVerdict(task.tests.size)
            return id
        }

        /**
         * Sends a compilation error verdict
         *
         * @param submissionId id of the submission
         * @param verdict compilation error verdict
         */
        fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.CompilationError) {
            holder[submissionId] = verdict
        }

        /**
         * Sends running task verdict
         *
         * @param submissionId id of the submission
         * @param verdict running verdict submission
         */
        fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.RunningVerdict) {
            holder[submissionId] = verdict
        }

        /**
         * Sends verdict of test to running task
         *
         * @param submissionId id of the submission
         * @param testId id of the test
         * @param verdict verdict of the test
         */
        fun sendVerdict(submissionId: Long, testId: Int, verdict: TestVerdict) {
            val task = holder[submissionId]
            if (task is SubmissionVerdict.RunningVerdict) {
                task.tests[testId] = verdict
            }
        }
    }
}