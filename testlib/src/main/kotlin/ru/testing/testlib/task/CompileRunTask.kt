package ru.testing.testlib.task

import ru.testing.testlib.limits.Limits

/**
 * Task which compile and run
 *
 * @property compile limits to compile
 * @property run limits to run
 *
 * @param tests tests of task
 */
open class CompileRunTask(tests: List<Test>, val compile: Limits, val run: Limits) : Task(tests)