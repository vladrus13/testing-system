package interfaces

import ru.testing.testlib.task.SubmissionVerdict
import ru.testing.testlib.task.Task
import ru.testing.testlib.task.TestVerdict

interface AbstractResultHolder {
    /**
     * Gets a verdict of submission
     *
     * @param submissionId id of submission
     * @return verdict
     */
    fun getVerdict(submissionId: Long): SubmissionVerdict?

    /**
     * Adds a task to holder
     *
     * @param task task
     * @return id of the submission
     */
    fun addTask(task: Task): Long

    /**
     * Sends a compilation error verdict
     *
     * @param submissionId id of the submission
     * @param verdict compilation error verdict
     */
    fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.CompilationError)

    /**
     * Sends running task verdict
     *
     * @param submissionId id of the submission
     * @param verdict running verdict submission
     */
    fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.RunningVerdict)

    /**
     * Sends compilation took too much time verdict
     *
     * @param submissionId id of the submission
     * @param verdict running verdict submission
     */
    fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.CompilationTimeLimit)

    /**
     * Sends verdict of test to running task
     *
     * @param submissionId id of the submission
     * @param testId id of the test
     * @param verdict verdict of the test
     */
    fun sendVerdict(submissionId: Long, testId: Int, verdict: TestVerdict)
}