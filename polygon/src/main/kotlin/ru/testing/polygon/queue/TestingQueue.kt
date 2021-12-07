package ru.testing.polygon.queue

import ru.testing.polygon.submission.SubmissionFile
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Testing queue. Contains all tasks to test
 *
 */
class TestingQueue {
    companion object {
        private val queue: ConcurrentLinkedQueue<SubmissionFile> = ConcurrentLinkedQueue<SubmissionFile>()

        /**
         * Adds a task to the queue
         *
         * @param task task
         */
        fun add(task: SubmissionFile) {
            queue.add(task)
        }

        /**
         * Gets the first task from queue and removes it from queue
         *
         * @return first task
         */
        fun get(): SubmissionFile? {
            return queue.poll()
        }
    }
}