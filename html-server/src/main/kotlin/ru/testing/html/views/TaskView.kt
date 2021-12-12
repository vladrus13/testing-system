package ru.testing.html.views

import kotlinx.html.BODY
import kotlinx.html.h2
import ru.testing.testlib.task.Task

internal fun BODY.taskView(tasksHolder: Map<Long, Task>) {
    for (task in tasksHolder.values) {
        h2 {
            text(task.title)
        }
        text(task.task)
    }
}