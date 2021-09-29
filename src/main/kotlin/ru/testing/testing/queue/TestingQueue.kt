package ru.testing.testing.queue

import ru.testing.testing.submission.SubmissionFile
import java.util.concurrent.ConcurrentLinkedQueue

class TestingQueue {
    companion object {
        private val queue: ConcurrentLinkedQueue<SubmissionFile> = ConcurrentLinkedQueue<SubmissionFile>()

        fun add(task: SubmissionFile) {
            queue.add(task)
        }

        fun get(): SubmissionFile? {
            return queue.poll()
        }
    }
}