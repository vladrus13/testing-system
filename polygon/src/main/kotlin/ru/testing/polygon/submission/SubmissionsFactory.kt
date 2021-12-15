package ru.testing.polygon.submission

import EnvironmentConfiguration
import interfaces.AbstractTypeOfLaunching
import interfaces.SubmissionFile
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
    fileType: AbstractTypeOfLaunching,
    task: Task,
    userId: Long
): SubmissionFile = with(configuration) {
    val id = resultHolder.addSubmission(task, userId)
    return SubmissionFile(id, title, source, fileType, task)
}