package ru.testing.testlib.task

import ru.testing.testlib.limits.Limits

/**
 * Task class. Contains information about the task
 *
 * @property textTests tests of task
 * @property title title of task
 * @property task task, text of task
 * @constructor constructor of task
 *
 * @see Limits
 */
sealed class Task(val title: String, val task: String, open val textTests: List<Test>)