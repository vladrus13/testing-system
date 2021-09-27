package ru.testing.plugins

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.routing.*

fun Application.module() {
    routing {
        get("/") {
            call.respondHtml {

            }

        }
    }
}
