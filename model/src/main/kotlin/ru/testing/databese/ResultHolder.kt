package ru.testing.databese

import interfaces.AbstractResultHolder
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.testing.databese.definition.Submissions
import ru.testing.databese.definition.TestVerdicts
import ru.testing.testlib.domain.Submission
import ru.testing.testlib.task.SubmissionVerdict
import ru.testing.testlib.task.Task
import ru.testing.testlib.task.TestVerdict

/**
 * Result holder. Temporary solution. Will be replaced by a database
 *
 */
class ResultHolder : AbstractResultHolder {
    /**
     * Gets a verdict of submission
     *
     * @param submissionId id of submission
     * @return verdict
     */
    override fun getVerdict(submissionId: Long): SubmissionVerdict? {
        val row = transaction {
            Submissions.select { Submissions.id eq submissionId }.firstOrNull()
        } ?: return null
        return mapToSubmission(row).verdict
    }

    /**
     * Adds a submission to holder
     *
     * @param task task
     * @param userId id of submitter
     * @return id of the submission
     */
    override fun addSubmission(task: Task, userId: Long): Long {
        val row = transaction {
            Submissions.insert {
                it[Submissions.userId] = userId
                it[Submissions.taskName] = task.title
                it[Submissions.testCount] = task.textTests.size
                it[Submissions.serializedStatus] =
                    Json.encodeToString<SubmissionVerdict>(SubmissionVerdict.NotLaunchedVerdict)
            }.resultedValues?.first() ?: throw RuntimeException("Could not save the submission in the database")
        }
        return row[Submissions.id]
    }

    /**
     * Sends a compilation error verdict
     *
     * @param submissionId id of the submission
     * @param verdict compilation error verdict
     */
    override fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.CompilationError) {
        doSendVerdict(submissionId, verdict)
    }

    /**
     * Sends running task verdict
     *
     * @param submissionId id of the submission
     * @param verdict running verdict submission
     */
    override fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.RunningVerdict) {
        doSendVerdict(submissionId, verdict)
    }

    /**
     * Sends compilation took too much time verdict
     *
     * @param submissionId id of the submission
     * @param verdict running verdict submission
     */
    override fun sendVerdict(submissionId: Long, verdict: SubmissionVerdict.CompilationTimeLimit) {
        doSendVerdict(submissionId, verdict)
    }

    /**
     * Sends verdict of test to running task
     *
     * @param submissionId id of the submission
     * @param testId id of the test
     * @param verdict verdict of the test
     */
    override fun sendTestVerdict(submissionId: Long, testId: Int, verdict: TestVerdict) {
        transaction {
            TestVerdicts.insert {
                it[TestVerdicts.testId] = testId
                it[TestVerdicts.submissionId] = submissionId
                it[TestVerdicts.serializedStatus] = Json.encodeToString(verdict)
            }
        }
    }

    private fun mapToTestVerdict(it: ResultRow): TestVerdict {
        return Json.decodeFromString(it[TestVerdicts.serializedStatus])
    }

    private fun mapToSubmission(it: ResultRow) = Submission(
        id = it[Submissions.id],
        userId = it[Submissions.userId],
        taskName = it[Submissions.taskName],
        verdict = mapToVerdict(it[Submissions.serializedStatus], it[Submissions.id], it[Submissions.testCount])
    )

    private fun mapToVerdict(serializedStatus: String, submissionId: Long, testCount: Int): SubmissionVerdict {
        val verdict = Json.decodeFromString<SubmissionVerdict>(serializedStatus)
        if (verdict is SubmissionVerdict.RunningVerdict) {
            val testList: ArrayList<TestVerdict> = ArrayList((1..testCount).map { TestVerdict.NL })
            transaction {
                TestVerdicts.select {
                    TestVerdicts.submissionId eq submissionId
                }.forEach {
                    testList[it[TestVerdicts.testId]] = Json.decodeFromString(it[TestVerdicts.serializedStatus])
                }
            }
            return SubmissionVerdict.RunningVerdict(testList)
        }
        return verdict
    }

    private fun doSendVerdict(submissionId: Long, verdict: SubmissionVerdict) {
        transaction {
            Submissions.update({ Submissions.id eq submissionId }) {
                it[Submissions.serializedStatus] = Json.encodeToString(verdict)
            }
        }
    }
}
