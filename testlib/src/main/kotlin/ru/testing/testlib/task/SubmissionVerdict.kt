package ru.testing.testlib.task

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
    data class CompilationError(val cause: String) : SubmissionVerdict()

    /**
     * Compilation took too much time verdict
     */
    object CompilationTimeLimit: SubmissionVerdict()

    /**
     * Running verdict
     *
     * @property tests verdicts of running
     */
    data class RunningVerdict(val tests: ArrayList<TestVerdict>) : SubmissionVerdict()

    /**
     * Not launched verdict
     */
    object NotLaunchedVerdict : SubmissionVerdict()
}