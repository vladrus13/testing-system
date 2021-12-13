package ru.testing.databese

import interfaces.AbstractResultHolder
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
class ResultHolder : AbstractResultHolder {
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
    override fun getVerdict(submissionId: Long) = holder[submissionId]

    /**
     * Adds a task to holder
     *
     * @param task task
     * @return id of the submission
     */
    override fun addTask(task: Task): Long {
        val id = futureId.incrementAndGet()
        holder[id] = SubmissionVerdict.NotLaunchedVerdict
        return id
    }

    /**
     * Sends a compilation error verdict
     *
     * @param submissionId id of the submission
     * @param verdict compilation error verdict
     */
    override fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.CompilationError) {
        holder[submissionId] = verdict
    }

    /**
     * Sends a compilation time exceeded verdict
     *
     * @param submissionId id of the submission
     * @param verdict compilation time exceeded verdict
     */
    override fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.CompilationTimeLimit) {
        holder[submissionId] = verdict
    }

    /**
     * Sends running task verdict
     *
     * @param submissionId id of the submission
     * @param verdict running verdict submission
     */
    override fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.RunningVerdict) {
        holder[submissionId] = verdict
    }

    /**
     * Sends verdict of test to running task
     *
     * @param submissionId id of the submission
     * @param testId id of the test
     * @param verdict verdict of the test
     */
    override fun sendVerdict(submissionId: Long, testId: Int, verdict: TestVerdict) {
        val task = holder[submissionId]
        if (task is SubmissionVerdict.RunningVerdict) {
            task.tests[testId] = verdict
        }
    }
}