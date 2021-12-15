package ru.testing.testlib.task

import kotlinx.serialization.Serializable

/**
 * Verdict of submission
 *
 */
@Serializable
sealed class SubmissionVerdict {

    /**
     * Compilation error verdict
     *
     * @property cause cause of compilation error
     */
    @Serializable
    data class CompilationError(val cause: String) : SubmissionVerdict()

    /**
     * Compilation took too much time verdict
     */
    @Serializable
    object CompilationTimeLimit: SubmissionVerdict()

    /**
     * Running verdict
     *
     * @property tests verdicts of running
     */
    @Serializable
    data class RunningVerdict(val tests: List<TestVerdict>) : SubmissionVerdict()

    /**
     * Not launched verdict
     */
    @Serializable
    object NotLaunchedVerdict : SubmissionVerdict()
}