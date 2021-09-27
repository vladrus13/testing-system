package ru.testing.plugins

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import kotlinx.html.*
import java.io.File

fun Application.module() {
    routing {
        get("/") {
            call.respondHtml {
                head {

                }
                body {
                    h1 {
                        text("Hello! This is a testing system!")
                    }
                    a("http://localhost:8080/chooseFile") {
                        +"Choose File"
                    }
                }
            }
        }
        get("/chooseFile") {
            call.respondHtml {
                head {

                }
                body {
                    form (method = FormMethod.post, encType = FormEncType.multipartFormData){
                        acceptCharset = "utf-8"
                        input(type = InputType.file, name = "chooseFile")
                        br()
                        submitInput(classes = "pure-button pure-button-primary") {
                            value = "Send"
                        }
                    }
                }
            }
        }
        post("/chooseFile") {
            val multipart = call.receiveMultipart()
            var title = ""
            var text : String? = null

            // Processes each part of the multipart input content of the user
            multipart.forEachPart { part ->
                if (part is PartData.FormItem) {
                    if (part.name == "title") {
                        title = part.value
                    }
                } else if (part is PartData.FileItem) {
                    part.streamProvider().use {
                        text = it.bufferedReader().use { it1 -> it1.readText() }
                    }
                }

                part.dispose()
            }
            if (text != null) {
                call.respondText(text!!)
            } else {
                call.respondText("Ban!!!")
            }
        }
    }
}
