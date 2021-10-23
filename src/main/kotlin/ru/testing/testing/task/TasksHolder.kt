package ru.testing.testing.task

import ru.testing.testing.task.tasks.APlusB
import ru.testing.testing.task.tasks.APlusBPlusC

class TasksHolder {
    companion object {
        val map: HashMap<Long, Task> = hashMapOf(
            Pair(1, APlusB()),
            Pair(2, APlusBPlusC()),
        )
    }
}