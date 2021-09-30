package ru.testing.testing.task

import ru.testing.testing.limits.Limits

/**
 * Task class. Contain information about task
 *
 * @property tests tests of task
 * @constructor constuctor of tast
 *
 * @param limits limits we get task
 *
 * @see Limits
 */
open class Task(val tests: List<Test>, limits: Limits)