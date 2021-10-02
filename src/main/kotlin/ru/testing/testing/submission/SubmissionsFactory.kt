package ru.testing.testing.submission

import ru.testing.testing.task.Task

class SubmissionsFactory {
    companion object {

        var id = 0L

        fun getInstance(title: String?, listing: String, fileType: SubmissionProcessFile, task: Task): SubmissionFile {
            return SubmissionFile(id++, title, listing, fileType, task)
        }
    }
}