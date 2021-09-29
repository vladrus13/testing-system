package ru.testing.server

import java.util.concurrent.Executors

class Server(count: Int) {
    private val list: List<MonoExecutor> = MutableList(count) { MonoExecutor() }
    private val executorService = Executors.newFixedThreadPool(count)

    fun stop() {
        list.forEach {
            it.stop()
        }
        executorService.shutdown()
    }

    fun run() {
        list.forEach {
            executorService.submit {
                it.run()
            }
        }
    }
}