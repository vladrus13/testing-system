package ru.testing.testing.server

import java.util.concurrent.Executors

/**
 * Executor server. Contain executors what tests solutions
 *
 * @constructor create server
 *
 * @param count count of executors
 */
class Server(count: Int) {
    private val list: List<MonoExecutor> = MutableList(count) { MonoExecutor() }
    private val executorService = Executors.newFixedThreadPool(count)

    /**
     * Stop server. All executors trying to stop taking tasks, after this all threads will shut down
     *
     */
    fun stop() {
        list.forEach {
            it.stop()
        }
        executorService.shutdown()
    }

    /**
     * Run server. It starts takes a tasks
     *
     */
    fun run() {
        list.forEach {
            executorService.submit {
                it.run()
            }
        }
    }
}