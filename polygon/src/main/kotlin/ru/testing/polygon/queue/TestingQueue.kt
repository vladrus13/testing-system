package ru.testing.polygon.queue

import interfaces.AbstractTestingQueue
import interfaces.SubmissionFile
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Testing queue. Contains all tasks to test
 *
 */
class TestingQueue: AbstractTestingQueue {
    private val queue: ConcurrentLinkedQueue<SubmissionFile> = ConcurrentLinkedQueue<SubmissionFile>()

    /**
     * Adds a task to the queue
     *
     * @param task task
     */
    override fun add(task: SubmissionFile) {
        queue.add(task)
    }

    /**
     * Gets the first task from queue and removes it from queue
     *
     * @return first task
     */
    override fun get(): SubmissionFile? {
        return queue.poll()
    }
}