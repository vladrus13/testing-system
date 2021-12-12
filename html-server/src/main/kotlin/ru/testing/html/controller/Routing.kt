package ru.testing.html.controller

import EnvironmentConfiguration
import interfaces.AbstractTypeOfLaunching
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.pipeline.*
import kotlinx.css.CssBuilder
import kotlinx.html.HTML
import kotlinx.html.div
import kotlinx.html.style
import org.mindrot.jbcrypt.BCrypt
import ru.testing.html.views.*
import ru.testing.html.views.utils.Viewer
import ru.testing.polygon.submission.makeSubmission
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
fun Application.module(configuration: EnvironmentConfiguration) = with(configuration) {
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
                if (validateUserCredentials(configuration, credentials)) {
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
        get("/register") {
            call.respondHtml { Viewer.getHTML(html = this, body = { registerView() }) }
        }
        post("/register") {
            handleUserRegister(configuration)
        }
        get("/tasks") {
            call.respondHtml {
                Viewer.getHTML(
                    html = this,
                    body = { taskView(configuration.tasksHolder) })
            }
        }
        authenticate("auth_session") {
            get("/") {
                call.respondHtml(block = HTML::indexView)
            }
            get("/chooseFile") {
                call.respondHtml {
                    Viewer.getHTML(
                        html = this,
                        body = { chooseFileView(configuration.tasksHolder, configuration.typeOfLaunchingHolder) })
                }
            }
            post("/chooseFile") {
                receiveTask(configuration)
            }
            get("/submission/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                    ?: return@get call.respondText("Invalid id: ${call.parameters["id"]}")
                val result = resultHolder.getVerdict(id)
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

private suspend fun PipelineContext<Unit, ApplicationCall>.handleUserRegister(configuration: EnvironmentConfiguration) =
    with(configuration) post@{
        val params = call.receiveParameters()
        val username = params["username"] ?: return@post call.respondText("Missing username")
        val password = params["password"] ?: return@post call.respondText("Missing password")
        val confirmation = params["confPassword"] ?: return@post call.respondText("Missing password confirmation")
        if (password.length < 5 || password.length > 40) return@post call.respondText("Password must be from 5 to 40 symbols")
        if (username.length < 3 || username.length > 25) return@post call.respondText("Username must be from 3 to 25 symbols")
        if (password != confirmation) return@post call.respondText("Password and confirmation does not match")
        configuration.userHolder.createUser(username, BCrypt.hashpw(password, BCrypt.gensalt(8)))
        call.respondRedirect("/login")
    }

private fun validateUserCredentials(
    configuration: EnvironmentConfiguration,
    credentials: UserPasswordCredential
): Boolean {
    val user = configuration.userHolder.findUserByName(credentials.name) ?: return false
    if (!BCrypt.checkpw(credentials.password, user.passwordHash)) return false
    return true
}

private suspend fun PipelineContext<Unit, ApplicationCall>.receiveTask(configuration: EnvironmentConfiguration) =
    with(configuration) {
        try {
            val multipart = call.receiveMultipart()
            var title = "Source"
            val (source, task, language) = run {
                var source: String? = null
                var task: Task? = null
                var language: AbstractTypeOfLaunching? = null
                multipart.forEachPart { part ->
                    if (part is PartData.FormItem) {
                        if (part.name == "chooseLanguage") {
                            for (typeOfLaunch in configuration.typeOfLaunchingHolder.getTypesOfLaunch()) {
                                if (typeOfLaunch.getCodeName() == part.value) language = typeOfLaunch
                            }
                            language ?: throw IllegalArgumentException("Unknown language: ${part.value}")
                        }
                        if (part.name == "chooseTask") {
                            task = configuration.tasksHolder[part.value.toLongOrNull()
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
                Triple(source, task, language)
            }
            require(task != null) { "Task is not specified" }
            require(source != null) { "Source is not specified" }
            require(language != null) { "Language is not specified" }
            val submission = makeSubmission(configuration, title, source, fileType = language, task)
            configuration.testingQueue.add(submission)
            call.respondRedirect(url = "http://localhost:8080/submission/${submission.id}")
        } catch (e: IllegalArgumentException) {
            call.respondText(e.message ?: "Bad submission")
        }
    }
