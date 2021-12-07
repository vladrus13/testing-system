package ru.testing.html.controller

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
    routing {
        get("/styles.css") {
            call.respondCss { testingSystemCss() }
        }
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
}

private suspend fun PipelineContext<Unit, ApplicationCall>.receiveTask() {
    var submissionProcessFile: SubmissionProcessFile = CPPSubmissionProcessFile()
    val multipart = call.receiveMultipart()
    var title = "Source"
    val (text, task) = run {
        var text: String? = null
        var task: Task? = null
        multipart.forEachPart { part ->
            if (part is PartData.FormItem) {
                if (part.name == "chooseLanguage") {
                    submissionProcessFile = when (part.value) {
                        "cpp" -> CPPSubmissionProcessFile()
                        "java" -> JavaSubmissionProcessFile()
                        else -> CPPSubmissionProcessFile() // todo: why cpp
                    }
                }
                if (part.name == "chooseTask") {
                    task = TasksHolder.map[part.value.toLong()] // todo: what if not long
                }
            }
            if (part is PartData.FileItem) {
                part.streamProvider().use { stream ->
                    title = part.originalFileName!! // todo what if null?
                    text = stream.bufferedReader().use { it.readText() }
                }
            }
            part.dispose()
        }
        text to task
    }
    when {
        task == null -> call.respondText("Ban!!!")
        text == null -> call.respondText("Ban!!!")
        else -> {
            val submission = SubmissionsFactory.getInstance(
                title = title,
                listing = text,
                fileType = submissionProcessFile,
                task = task
            )
            TestingQueue.add(submission)
            call.respondRedirect(url = "http://localhost:8080/submission/${submission.id}")
        }
    }
}
