package ru.testing.html.views

import kotlinx.html.*
import ru.testing.testlib.domain.Submission
import ru.testing.testlib.task.SubmissionVerdict
import ru.testing.testlib.task.TestVerdict

internal fun BODY.submissionsView(
    submissions: List<Submission>,
) {
    p { text("My latest submissions") }
    table(classes = "verdicts") {
        tr {
            th {
                text("ID")
            }
            th {
                text("Verdict")
            }
        }
        submissions.forEach {
            // TODO: How to move it to functions? td/tr is invisible to them
            val text: String
            var cssClass : String? = null
            when (it.verdict) {
                is SubmissionVerdict.CompilationError -> text = "Compilation error"
                is SubmissionVerdict.CompilationTimeLimit -> text = "Compilation time limit exceeded"
                is SubmissionVerdict.NotLaunchedVerdict -> text = "Not launched"
                is SubmissionVerdict.RunningVerdict -> {
                    val tests = (it.verdict as SubmissionVerdict.RunningVerdict).tests
                    if (tests.contains(TestVerdict.NL)) {
                        text = "Running..."
                    } else {
                        val correct =
                            tests.fold(0) { acc, testVerdict -> if (testVerdict is TestVerdict.OK) acc + 1 else acc }
                        cssClass = if (correct == tests.size) "ok_verdict" else "wa_verdict"
                        text = "Tests: %d/%d".format(correct, tests.size)
                    }
                }
            }
            tr(classes = cssClass) {
                td {
                    a("/submission/" + it.id) {
                        text(it.id)
                    }
                }
                td {
                    text(text)
                }
            }
        }
    }
}



