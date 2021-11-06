package ru.testing.polygon.submission

import ru.testing.polygon.database.ResultHolder
import ru.testing.testlib.task.Task

/**
 * Submission factory
 *
 */
class SubmissionsFactory {
    companion object {

        /**
         * Get instance of task
         *
         * @param title title of task
         * @param listing listing of task
         * @param fileType submission process file
         * @param task task
         * @return instance of task
         */
        fun getInstance(title: String?, listing: String, fileType: SubmissionProcessFile, task: Task): SubmissionFile {
            val id = ResultHolder.addTask(task)
            return SubmissionFile(id, title, listing, fileType, task)
        }
    }
}