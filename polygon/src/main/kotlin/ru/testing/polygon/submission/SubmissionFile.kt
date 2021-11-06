package ru.testing.polygon.submission

import ru.testing.testlib.task.Task

/**
 * Submission of participant. We must test it
 *
 * @property id id of submission
 * @property title title of file (name of file)
 * @property listing code of this file
 * @property fileType type of submission
 * @property task on which task we test thins submission
 */
class SubmissionFile(
    val id: Long,
    val title: String?,
    val listing: String,
    val fileType: SubmissionProcessFile,
    val task: Task
)