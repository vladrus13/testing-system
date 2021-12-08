package ru.testing.testlib.task

import ru.testing.testlib.limits.Limits

/**
 * Task class. Contains information about the task
 *
 * @property tests tests of task
 * @constructor constructor of task
 *
 * @see Limits
 */
sealed class Task(open val tests: List<Test>)