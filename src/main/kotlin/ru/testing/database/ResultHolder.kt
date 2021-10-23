package ru.testing.database

import ru.testing.testing.task.SubmissionVerdict
import ru.testing.testing.task.Task
import ru.testing.testing.task.TestVerdict
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

        fun getVerdict(idSubmission: Long) = holder[idSubmission]

        fun addTask(task: Task): Long {
            val id = futureId.incrementAndGet()
            holder[id] = SubmissionVerdict.NotLaunchedVerdict(task.tests.size)
            return id
        }

        fun sendVerdict(idSubmission: Long, verdict: SubmissionVerdict.CompilationError) {
            holder[idSubmission] = verdict
        }

        fun sendVerdict(idSubmission: Long, verdict: SubmissionVerdict.RunningVerdict) {
            holder[idSubmission] = verdict
        }

        fun sendVerdict(idSubmission: Long, idTest: Int, verdict: TestVerdict) {
            val task = holder[idSubmission]
            if (task is SubmissionVerdict.RunningVerdict) {
                task.tests[idTest] = verdict
            }
        }
    }
}