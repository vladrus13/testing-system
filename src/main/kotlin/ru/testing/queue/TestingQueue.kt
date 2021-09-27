package ru.testing.queue

import ru.testing.task.Task
import java.util.concurrent.ConcurrentLinkedQueue

class TestingQueue {
    companion object {
        val queue : ConcurrentLinkedQueue<Task> = ConcurrentLinkedQueue<Task>()
    }
}