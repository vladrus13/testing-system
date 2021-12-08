package ru.testing.html.controller

import EnvironmentConfiguration
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import kotlinx.css.*
import kotlinx.html.*
import ru.testing.html.views.chooseFileView
import ru.testing.html.views.indexView
import ru.testing.html.views.submissionResultView
import ru.testing.html.views.testingSystemCss
import ru.testing.html.views.utils.Viewer
import ru.testing.polygon.submission.*
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
    routing {
        get("/styles.css") {
            call.respondCss { testingSystemCss() }
        }
        get("/") {
            call.respondHtml(block = HTML::indexView)
        }
        get("/chooseFile") {
            call.respondHtml { Viewer.getHTML(html = this, body = { chooseFileView(configuration.tasksHolder) }) }
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
}

private suspend fun PipelineContext<Unit, ApplicationCall>.receiveTask(configuration: EnvironmentConfiguration) = with(configuration) {
    try {
        val multipart = call.receiveMultipart()
        var title = "Source"
        val (source, task, language) = run {
            var source: String? = null
            var task: Task? = null
            var language: ProgrammingLanguage? = null
            multipart.forEachPart { part ->
                if (part is PartData.FormItem) {
                    if (part.name == "chooseLanguage") {
                        language = when (part.value) {
                            "cpp" -> Cpp
                            "java" -> Java
                            else -> throw IllegalArgumentException("Unknown language: ${part.value}")
                        }
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
