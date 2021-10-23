package ru.testing.testing.task

import ru.testing.testing.limits.Limits

open class CompileRunTask(id: Int, tests: List<Test>, val compile: Limits, val run: Limits) : Task(id, tests)