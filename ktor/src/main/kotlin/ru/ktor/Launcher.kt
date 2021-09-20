package ru.ktor

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.ktor.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
        configureRouting()
    }.start(wait = true)
}