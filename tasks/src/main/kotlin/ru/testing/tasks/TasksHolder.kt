package ru.testing.tasks

import ru.testing.tasks.holder.APlusB
import ru.testing.tasks.holder.APlusBPlusC
import ru.testing.testlib.task.Task

/**
 * Tasks holder
 *
 */
class TasksHolder {
    companion object {
        /**
         * Tasks
         */
        val map: HashMap<Long, Task> = hashMapOf(
            Pair(1, APlusB()),
            Pair(2, APlusBPlusC()),
        )
    }
}