package ru.testing

import SimpleEnvironmentConfiguration
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.testing.databese.DatabaseInitializer
import ru.testing.databese.ResultHolder
import ru.testing.databese.UserHolder
import ru.testing.html.controller.module
import ru.testing.polygon.database.TypeOfLaunchingHolder
import ru.testing.polygon.queue.TestingQueue
import ru.testing.polygon.server.Executors
import ru.testing.tasks.TasksHolder

/**
 * Main class. Launch servers
 *
 */
fun main() {
    val configuration = SimpleEnvironmentConfiguration(
        TasksHolder(),
        TestingQueue(),
        Executors(1),
        ResultHolder(),
        UserHolder(),
        DatabaseInitializer(),
        TypeOfLaunchingHolder()
    )
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module(configuration)
    }.start(wait = true)
}
