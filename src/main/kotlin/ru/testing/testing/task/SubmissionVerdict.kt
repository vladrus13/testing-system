package ru.testing.testing.task

sealed class SubmissionVerdict {
    class CompilationError(val cause: String) : SubmissionVerdict()

    class RunningVerdict(val tests: ArrayList<TestVerdict>) : SubmissionVerdict()

    class NotLaunchedVerdict(val countTests: Int) : SubmissionVerdict()
}