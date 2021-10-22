package ru.testing.testing.task

import ru.testing.testing.limits.Limits

open class CompileRunTask(tests: List<Test>, val compile: Limits, val run: Limits) : Task(tests)