package ru.testing.polygon.server

import EnvironmentConfiguration
import interfaces.AbstractExecutors
import java.util.concurrent.Executors


/**
 * Executor servers. Contains executors to tests solutions
 *
 * @constructor creates server
 *
 * @param count count of executors
 */
class Executors(count: Int) : AbstractExecutors {
    private val list: List<MonoExecutor> = MutableList(count) { MonoExecutor() }
    private val executorService = Executors.newFixedThreadPool(count)

    /**
     * Stops server. All executors try to stop taking tasks, after this all threads will shut down
     *
     */
    override fun stop() {
        list.forEach {
            it.stop()
        }
        executorService.shutdown()
    }

    /**
     * Runs server. It starts handling tasks
     *
     */
    override fun run(configuration: EnvironmentConfiguration) {
        for (it in list) {
            executorService.submit {
                try {
                    it.run(configuration)
                } catch (e: Throwable) {
                    e.printStackTrace()
                } finally {

                }
            }
        }
    }
}