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
         * @param title task title
         * @param source solution source
         * @param fileType submission process file
         * @param task task
         * @return task instance
         */
        fun getInstance(title: String, source: String, fileType: SubmissionProcessFile, task: Task): SubmissionFile {
            val id = ResultHolder.addTask(task)
            return SubmissionFile(id, title, source, fileType, task)
        }
    }
}