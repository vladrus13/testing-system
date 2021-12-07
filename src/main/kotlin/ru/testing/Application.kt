package ru.testing

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.testing.html.controller.module
import ru.testing.polygon.server.Executors

/**
 * Main class. Launch servers
 *
 */
fun main() {
    Executors(1).run()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}
