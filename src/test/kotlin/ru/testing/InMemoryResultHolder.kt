package ru.testing

import interfaces.AbstractResultHolder
import ru.testing.testlib.domain.Submission
import ru.testing.testlib.task.SubmissionVerdict
import ru.testing.testlib.task.Task
import ru.testing.testlib.task.TestVerdict

class InMemoryResultHolder : AbstractResultHolder {
    private val submissions: MutableMap<Long, SubmissionVerdict> = mutableMapOf()
    private val submissionsByUser: MutableMap<Long, MutableList<Submission>> = mutableMapOf()
    private var newId = 1L
    private val testsCountBySubmissions: MutableMap<Long, Int> = mutableMapOf()

    override fun getVerdict(submissionId: Long): SubmissionVerdict? = submissions[submissionId]

    override fun addSubmission(task: Task, userId: Long): Long {
        val submission = Submission(newId++, userId, task.title, SubmissionVerdict.NotLaunchedVerdict)
        submissionsByUser.getOrPut(userId) { mutableListOf() }.add(submission)
        submissions[submission.id] = SubmissionVerdict.NotLaunchedVerdict
        testsCountBySubmissions[submission.id] = task.textTests.size
        return submission.id
    }

    override fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.CompilationError) {
        submissions[submissionId] = verdict
    }

    override fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.RunningVerdict) {
        assert(verdict.tests.isEmpty())
        submissions[submissionId] =
            verdict.copy(tests = List(testsCountBySubmissions[submissionId]!!) { TestVerdict.NL })
    }

    override fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.CompilationTimeLimit) {
        submissions[submissionId] = verdict
    }

    override fun sendTestVerdict(submissionId: Long, testId: Int, verdict: TestVerdict) {
        val runningVerdict = submissions[submissionId] as SubmissionVerdict.RunningVerdict
        submissions[submissionId] =
            SubmissionVerdict.RunningVerdict(runningVerdict.tests.toMutableList().apply { this[testId] = verdict })
    }

    override fun getLastSubmissions(userId: Long, submissionCount: Int): List<Submission> =
        submissionsByUser.getOrPut(userId) { mutableListOf() }.takeLast(submissionCount)
}