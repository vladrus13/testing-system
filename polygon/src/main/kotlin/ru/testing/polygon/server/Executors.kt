package ru.testing.polygon.server

import java.util.concurrent.Executors

/**
 * Executor servers. Contains executors to tests solutions
 *
 * @constructor creates server
 *
 * @param count count of executors
 */
class Executors(count: Int) {
    private val list: List<MonoExecutor> = MutableList(count) { MonoExecutor() }
    private val executorService = Executors.newFixedThreadPool(count)

    /**
     * Stops server. All executors try to stop taking tasks, after this all threads will shut down
     *
     */
    fun stop() {
        list.forEach {
            it.stop()
        }
        executorService.shutdown()
    }

    /**
     * Runs server. It starts handling tasks
     *
     */
    fun run() {
        list.forEach {
            executorService.submit {
                try {
                    it.run()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }
}