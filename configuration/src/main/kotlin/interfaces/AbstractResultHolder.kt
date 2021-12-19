package interfaces

import ru.testing.testlib.domain.Submission
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
     * Adds a submission to holder
     *
     * @param task task
     * @param userId id of submitter
     * @return id of the submission
     */
    fun addSubmission(task: Task, userId: Long): Long

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
    fun sendTestVerdict(submissionId: Long, testId: Int, verdict: TestVerdict)

    /**
     * Receive the latest submissions of user
     * @param submissionCount how many submission to receive
     * @return list of submissions
     */
    fun getLastSubmissions(userId: Long, submissionCount: Int) : List<Submission>
}
