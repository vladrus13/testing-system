package interfaces

import ru.testing.testlib.task.Task

/**
 * Submission of participant. We must test it
 *
 * @property id id of submission
 * @property title title of file (name of file)
 * @property source code of this file
 * @property processFile a process file
 * @property task on which task we test thins submission
 */
class SubmissionFile(
    val id: Long,
    val title: String,
    val source: String,
    val processFile: AbstractTypeOfLaunching,
    val task: Task
)
