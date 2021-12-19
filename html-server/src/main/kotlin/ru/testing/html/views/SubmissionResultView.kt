package ru.testing.html.views

import kotlinx.html.BODY
import kotlinx.html.table
import kotlinx.html.th
import kotlinx.html.tr
import ru.testing.testlib.task.SubmissionVerdict

internal fun BODY.submissionResultView(
    result: SubmissionVerdict?,
    id: Long
) {
    when (result) {
        is SubmissionVerdict.CompilationError -> text(result.cause)
        is SubmissionVerdict.RunningVerdict -> table(classes = "verdicts") {
            tr {
                th {
                    text("Test")
                }
                th {
                    text("Verdict")
                }
            }
            result.tests.forEachIndexed { index, test ->
                test.toRow(index, this)
            }
        }
        is SubmissionVerdict.NotLaunchedVerdict -> text("Not launched")
        null -> text("Id $id not found!")
    }
}
