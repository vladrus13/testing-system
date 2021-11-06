package ru.testing.polygon.database

import ru.testing.testlib.task.SubmissionVerdict
import ru.testing.testlib.task.Task
import ru.testing.testlib.task.TestVerdict
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Result holder. Temporary solution. Will be replaced by database
 *
 */
class ResultHolder {
    companion object {
        /**
         * Holds all results by it id
         */
        private val holder: ConcurrentMap<Long, SubmissionVerdict> = ConcurrentHashMap()

        private val futureId: AtomicLong = AtomicLong(0)

        /**
         * Get verdict of submission
         *
         * @param idSubmission id of submission
         * @return verdict
         */
        fun getVerdict(idSubmission: Long) = holder[idSubmission]

        /**
         * Add task to holder
         *
         * @param task task
         * @return id of submission
         */
        fun addTask(task: Task): Long {
            val id = futureId.incrementAndGet()
            holder[id] = SubmissionVerdict.NotLaunchedVerdict(task.tests.size)
            return id
        }

        /**
         * Send compilation error verdict
         *
         * @param idSubmission id of submission
         * @param verdict compilation error verdict
         */
        fun sendVerdict(idSubmission: Long, verdict: SubmissionVerdict.CompilationError) {
            holder[idSubmission] = verdict
        }

        /**
         * Send running task verdict
         *
         * @param idSubmission id of submission
         * @param verdict running verdict submission
         */
        fun sendVerdict(idSubmission: Long, verdict: SubmissionVerdict.RunningVerdict) {
            holder[idSubmission] = verdict
        }

        /**
         * Send verdict of test to running task
         *
         * @param idSubmission id of submission
         * @param idTest id of test
         * @param verdict verdict of test
         */
        fun sendVerdict(idSubmission: Long, idTest: Int, verdict: TestVerdict) {
            val task = holder[idSubmission]
            if (task is SubmissionVerdict.RunningVerdict) {
                task.tests[idTest] = verdict
            }
        }
    }
}