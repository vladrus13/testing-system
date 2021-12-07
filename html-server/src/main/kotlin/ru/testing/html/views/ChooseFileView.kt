package ru.testing.html.controller

import kotlinx.html.*
import ru.testing.tasks.TasksHolder

internal fun BODY.chooseFileView() {
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