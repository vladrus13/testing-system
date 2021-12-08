package interfaces

import EnvironmentConfiguration

interface AbstractExecutors {
    /**
     * Stops server. All executors try to stop taking tasks, after this all threads will shut down
     *
     */
    fun stop()

    /**
     * Runs server. It starts handling tasks
     *
     */
    fun run(configuration: EnvironmentConfiguration)
}