package ru.testing.polygon.submission

import EnvironmentConfiguration
import interfaces.SubmissionFile
import interfaces.SubmissionProcessFile
import ru.testing.polygon.database.ResultHolder
import ru.testing.testlib.task.Task

/**
 * Get instance of task
 *
 * @param title task title
 * @param source solution source
 * @param fileType submission process file
 * @param task task
 * @return task instance
 */
fun makeSubmission(
    configuration: EnvironmentConfiguration,
    title: String,
    source: String,
    fileType: SubmissionProcessFile,
    task: Task
): SubmissionFile = with(configuration) {
    val id = resultHolder.addTask(task)
    return SubmissionFile(id, title, source, fileType, task)
}