package ru.testing.testing.submission

import ru.testing.database.ResultHolder
import ru.testing.testing.task.Task

class SubmissionsFactory {
    companion object {

        fun getInstance(title: String?, listing: String, fileType: SubmissionProcessFile, task: Task): SubmissionFile {
            val id = ResultHolder.addTask(task)
            return SubmissionFile(id, title, listing, fileType, task)
        }
    }
}