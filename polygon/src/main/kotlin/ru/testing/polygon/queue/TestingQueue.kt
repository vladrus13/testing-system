package ru.testing.polygon.queue

import ru.testing.polygon.submission.SubmissionFile
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Testing queue. Contain all tasks what we must test
 *
 */
class TestingQueue {
    companion object {
        private val queue: ConcurrentLinkedQueue<SubmissionFile> = ConcurrentLinkedQueue<SubmissionFile>()

        /**
         * Add task to the queue
         *
         * @param task task
         */
        fun add(task: SubmissionFile) {
            queue.add(task)
        }

        /**
         * Get first task from queue and remove it from queue
         *
         * @return first task
         */
        fun get(): SubmissionFile? {
            return queue.poll()
        }
    }
}