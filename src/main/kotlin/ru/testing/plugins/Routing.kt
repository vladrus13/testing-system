package ru.testing.plugins

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*
import ru.testing.database.ResultHolder
import ru.testing.testing.queue.TestingQueue
import ru.testing.testing.submission.CPPSubmissionProcessFile
import ru.testing.testing.submission.JavaSubmissionProcessFile
import ru.testing.testing.submission.SubmissionProcessFile
import ru.testing.testing.submission.SubmissionsFactory
import ru.testing.testing.task.APlusB

/**
 * Routing function
 *
 */
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
                    form(method = FormMethod.post, encType = FormEncType.multipartFormData) {
                        acceptCharset = "utf-8"
                        select {
                            this.name = "chooseLanguage"
                            option {
                                value = "java"
                                text("Java")
                            }
                            option {
                                value = "cpp"
                                text("C++")
                            }
                        }
                        br()
                        input(type = InputType.file, name = "chooseFile") {
                            required = true
                        }
                        br()
                        br()
                        submitInput(classes = "pure-button pure-button-primary") {
                            value = "Send"
                        }
                    }
                }
            }
        }
        post("/chooseFile") {
            var submissionProcessFile: SubmissionProcessFile = CPPSubmissionProcessFile()
            val multipart = call.receiveMultipart()
            var title = "Source"
            var text: String? = null
            multipart.forEachPart { part ->
                if (part is PartData.FormItem) {
                    if (part.name == "chooseLanguage") {
                        submissionProcessFile = when (part.value) {
                            "cpp" -> CPPSubmissionProcessFile()
                            "java" -> JavaSubmissionProcessFile()
                            else -> CPPSubmissionProcessFile()
                        }
                    }
                }
                if (part is PartData.FileItem) {
                    part.streamProvider().use {
                        title = part.originalFileName!!
                        text = it.bufferedReader().use { it1 -> it1.readText() }
                    }
                }
                part.dispose()
            }
            if (text != null) {
                val task = SubmissionsFactory.getInstance(
                    title = title,
                    listing = text!!,
                    fileType = submissionProcessFile,
                    task = APlusB()
                )
                TestingQueue.add(task)
                call.respondRedirect(url = "http://localhost:8080/submission/${task.id}")
            } else {
                call.respondText("Ban!!!")
            }
        }
        get("/submission/{id}") {
            val id = call.parameters["id"]!!.toLong()
            val result = ResultHolder.holder[id]
            if (result == null) {
                call.respondText("Id $id not found!")
            } else {
                call.respondText(result.joinToString(separator = "\n") {
                    it.toString()
                })
            }
        }
    }
}
