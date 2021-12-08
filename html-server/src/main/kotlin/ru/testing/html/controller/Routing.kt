package ru.testing.html.controller

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.pipeline.*
import kotlinx.css.*
import kotlinx.html.*
import ru.testing.html.views.loginView
import ru.testing.html.views.utils.Viewer
import ru.testing.polygon.database.ResultHolder
import ru.testing.polygon.queue.TestingQueue
import ru.testing.polygon.submission.CPPSubmissionProcessFile
import ru.testing.polygon.submission.JavaSubmissionProcessFile
import ru.testing.polygon.submission.SubmissionProcessFile
import ru.testing.polygon.submission.SubmissionsFactory
import ru.testing.tasks.TasksHolder
import ru.testing.testlib.task.Task

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
    install(Sessions) {
        cookie<UserIdPrincipal>("user_session") {
            // Configure session authentication
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 60  // 1 hour
        }
    }

    install(Authentication) {
        session<UserIdPrincipal>("auth_session") {
            validate { session ->
                session  // TODO: maybe we need to store user id somehow?
            }
            challenge {
                call.respondRedirect("/login")
            }
        }

        form("auth_form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                if (credentials.name == "admin" && credentials.password == "12345") {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
            challenge {
                call.respondHtml {
                    Viewer.getHTML(html = this, body = {
                        loginView()
                        div {
                            style = "color:red;"
                            text("Incorrect username or password")
                        }
                    })
                }
            }
        }
    }

    routing {
        get("/styles.css") {
            call.respondCss { testingSystemCss() }
        }
        get("/logout") {
            call.sessions.clear<UserIdPrincipal>()
            call.respondRedirect("/login")
        }
        get("/login") {
            call.respondHtml { Viewer.getHTML(html = this, body = { loginView() }) }
        }
        authenticate("auth_session") {
            get("/") {
                call.respondHtml(block = HTML::indexView)
            }
            get("/chooseFile") {
                call.respondHtml { Viewer.getHTML(html = this, body = { chooseFileView() }) }
            }
            post("/chooseFile") {
                receiveTask()
            }
            get("/submission/{id}") {
                val id = call.parameters["id"]!!.toLong()
                val result = ResultHolder.getVerdict(id)
                call.respondHtml { Viewer.getHTML(html = this, body = { submissionResultView(result, id) }) }
            }
        }
        authenticate("auth_form") {
            post("/login") {
                val principal = call.principal<UserIdPrincipal>()
                call.sessions.set(principal)
                call.respondRedirect("/")
            }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.receiveTask() {
    try {
        var submissionProcessFile: SubmissionProcessFile = CPPSubmissionProcessFile()
        val multipart = call.receiveMultipart()
        var title = "Source"
        val (source, task) = run {
            var source: String? = null
            var task: Task? = null
            multipart.forEachPart { part ->
                if (part is PartData.FormItem) {
                    if (part.name == "chooseLanguage") {
                        submissionProcessFile = when (part.value) {
                            "cpp" -> CPPSubmissionProcessFile()
                            "java" -> JavaSubmissionProcessFile()
                            else -> throw IllegalArgumentException("Unknown language: ${part.value}")
                        }
                    }
                    if (part.name == "chooseTask") {
                        task = TasksHolder.map[part.value.toLongOrNull()
                            ?: throw IllegalArgumentException("Unknown task id: ${part.value}")]
                    }
                }
                if (part is PartData.FileItem) {
                    part.streamProvider().use { stream ->
                        title = part.originalFileName ?: throw IllegalArgumentException("File name not provided")
                        source = stream.bufferedReader().use { it.readText() }
                    }
                }
                part.dispose()
            }
            source to task
        }
        require(task != null) { "Task is not specified" }
        require(source != null) { "Source is not specified" }
        val submission = SubmissionsFactory.getInstance(
            title = title,
            source = source,
            fileType = submissionProcessFile,
            task = task
        )
        TestingQueue.add(submission)
        call.respondRedirect(url = "http://localhost:8080/submission/${submission.id}")
    } catch (e: IllegalArgumentException) {
        call.respondText(e.message ?: "Bad submission")
    }
}
