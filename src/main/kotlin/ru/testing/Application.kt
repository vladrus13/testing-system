package ru.testing

import SimpleEnvironmentConfiguration
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.testing.html.controller.module
import ru.testing.polygon.database.DatabaseInitializer
import ru.testing.polygon.database.ResultHolder
import ru.testing.polygon.database.UserHolder
import ru.testing.polygon.queue.TestingQueue
import ru.testing.polygon.server.Executors
import ru.testing.tasks.TasksHolder

/**
 * Main class. Launch servers
 *
 */
fun main() {
    val configuration = SimpleEnvironmentConfiguration(TasksHolder(), TestingQueue(), Executors(1), ResultHolder(), UserHolder(), DatabaseInitializer())
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module(configuration)
    }.start(wait = true)
}
