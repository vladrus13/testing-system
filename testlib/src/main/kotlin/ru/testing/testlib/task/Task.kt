package ru.testing.testlib.task

import ru.testing.testlib.limits.Limits

/**
 * Task class. Contain information about task
 *
 * @property tests tests of task
 * @constructor constructor of task
 *
 * @see Limits
 */
open class Task(open val tests: List<Test>)