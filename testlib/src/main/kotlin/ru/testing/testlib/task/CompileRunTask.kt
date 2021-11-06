package ru.testing.testlib.task

import ru.testing.testlib.limits.Limits

/**
 * Task which compile and run
 *
 * @property compile limits to compile
 * @property run limits to run
 *
 * @param id id of tasks
 * @param tests tests of task
 */
open class CompileRunTask(id: Int, tests: List<Test>, val compile: Limits, val run: Limits) : Task(id, tests)