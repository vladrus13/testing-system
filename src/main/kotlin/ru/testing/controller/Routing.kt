package ru.testing.controller

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.css.*
import kotlinx.html.*
import ru.testing.database.ResultHolder
import ru.testing.testing.queue.TestingQueue
import ru.testing.testing.submission.CPPSubmissionProcessFile
import ru.testing.testing.submission.JavaSubmissionProcessFile
import ru.testing.testing.submission.SubmissionProcessFile
import ru.testing.testing.submission.SubmissionsFactory
import ru.testing.testing.task.SubmissionVerdict
import ru.testing.testing.task.Task
import ru.testing.testing.task.TasksHolder
import ru.testing.view.Viewer

/**
 * Respond of css
 *
 * @param builder css builder
 */
suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

/**
 * Routing function
 *
 */
fun Application.module() {
    routing {
        get("/styles.css") {
            call.respondCss {
                rule(".verdicts .ok_verdict") {
                    backgroundColor = rgb(216, 256, 216)
                }
                rule(".verdicts .wa_verdict") {
                    backgroundColor = rgb(256, 216, 216)
                }
                rule(".verdicts .tl_verdict") {
                    backgroundColor = rgb(216, 216, 256)
                }
                rule(".verdicts .re_verdict") {
                    backgroundColor = rgb(256, 256, 216)
                }
                rule(".verdicts") {
                    border = "1px"
                    borderStyle = BorderStyle.solid
                    borderColor = Color.black
                }
            }
        }
        get("/") {
            call.respondHtml {
                Viewer.getHTML(
                    html = this,
                    body = {
                        h1 {
                            text("Hello! This is a testing system!")
                        }
                        a("http://localhost:8080/chooseFile") {
                            +"Choose File"
                        }
                    }
                )
            }
        }
        get("/chooseFile") {
            call.respondHtml {
                Viewer.getHTML(
                    html = this,
                    body = {
                        form(method = FormMethod.post, encType = FormEncType.multipartFormData) {
                            acceptCharset = "utf-8"
                            select {
                                this.name = "chooseTask"
                                for (tasks in TasksHolder.map) {
                                    option {
                                        value = tasks.key.toString()
                                        text(tasks.value::class.java.simpleName)
                                    }
                                }
                            }
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
                )
            }
        }
        post("/chooseFile") {
            var submissionProcessFile: SubmissionProcessFile = CPPSubmissionProcessFile()
            val multipart = call.receiveMultipart()
            var title = "Source"
            var text: String? = null
            var task: Task? = null
            multipart.forEachPart { part ->
                if (part is PartData.FormItem) {
                    if (part.name == "chooseLanguage") {
                        submissionProcessFile = when (part.value) {
                            "cpp" -> CPPSubmissionProcessFile()
                            "java" -> JavaSubmissionProcessFile()
                            else -> CPPSubmissionProcessFile()
                        }
                    }
                    if (part.name == "chooseTask") {
                        task = TasksHolder.map[part.value.toLong()]
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
            if (task == null) {
                call.respondText("Ban!!!")
                return@post
            }
            if (text == null) {
                call.respondText("Ban!!!")
                return@post
            }
            val submission = SubmissionsFactory.getInstance(
                title = title,
                listing = text!!,
                fileType = submissionProcessFile,
                task = task!!
            )
            TestingQueue.add(submission)
            call.respondRedirect(url = "http://localhost:8080/submission/${submission.id}")
        }
        get("/submission/{id}") {
            val id = call.parameters["id"]!!.toLong()
            val result = ResultHolder.getVerdict(id)
            call.respondHtml {
                Viewer.getHTML(
                    html = this,
                    body = {
                        when (result) {
                            is SubmissionVerdict.CompilationError -> {
                                text(result.cause)
                            }
                            is SubmissionVerdict.RunningVerdict -> {
                                table(classes = "verdicts") {
                                    tr {
                                        th {
                                            text("Test")
                                        }
                                        th {
                                            text("Verdict")
                                        }
                                    }
                                    result.tests.forEachIndexed { index, test ->
                                        test.toRow(index, this)
                                    }
                                }
                            }
                            is SubmissionVerdict.NotLaunchedVerdict -> {
                                text("Not launched")
                            }
                            null -> {
                                text("Id $id not found!")
                            }
                        }
                    }
                )
            }
        }
    }
}
