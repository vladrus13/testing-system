package ru.testing.testlib.domain

import ru.testing.testlib.task.SubmissionVerdict

data class Submission(
    val id: Long,
    val userId: Long,
    val taskName: String,
    val verdict: SubmissionVerdict
)
