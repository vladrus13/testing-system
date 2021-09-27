package ru.testing.queue

import ru.testing.task.Task
import java.util.concurrent.ConcurrentLinkedQueue

class TestingQueue {
    companion object {
        private val queue: ConcurrentLinkedQueue<Task> = ConcurrentLinkedQueue<Task>()

        fun add(task: Task) {
            queue.add(task)
        }

        fun get(): Task? {
            return queue.poll()
        }
    }
}