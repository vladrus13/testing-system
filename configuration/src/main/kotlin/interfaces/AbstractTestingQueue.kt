package interfaces

interface AbstractTestingQueue {

    /**
     * Adds a task to the queue
     *
     * @param task task
     */
    fun add(task: SubmissionFile)

    /**
     * Gets the first task from queue and removes it from queue
     *
     * @return first task
     */
    fun get(): SubmissionFile?
}
