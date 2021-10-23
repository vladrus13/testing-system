package ru.testing.testing.task

/**
 * Verdict of submission
 *
 */
sealed class SubmissionVerdict {

    /**
     * Compilation error verdict
     *
     * @property cause cause of compilation error
     */
    class CompilationError(val cause: String) : SubmissionVerdict()

    /**
     * Running verdict
     *
     * @property tests verdicts of running
     */
    class RunningVerdict(val tests: ArrayList<TestVerdict>) : SubmissionVerdict()

    /**
     * Not launched verdict
     *
     * @property countTests count of tests
     */
    class NotLaunchedVerdict(private val countTests: Int) : SubmissionVerdict()
}