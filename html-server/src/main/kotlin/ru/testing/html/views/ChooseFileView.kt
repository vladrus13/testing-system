package ru.testing.html.views

import interfaces.AbstractTypeOfLaunchingHolder
import kotlinx.html.*
import ru.testing.testlib.task.Task

internal fun BODY.chooseFileView(tasksHolder: Map<Long, Task>, typeOfLaunchingHolder: AbstractTypeOfLaunchingHolder) {
    form(method = FormMethod.post, encType = FormEncType.multipartFormData) {
        acceptCharset = "utf-8"
        select {
            this.name = "chooseTask"
            for (tasks in tasksHolder) {
                option {
                    value = tasks.key.toString()
                    text(tasks.value::class.java.simpleName)
                }
            }
        }
        select {
            this.name = "chooseLanguage"
            for (typeOfLaunch in typeOfLaunchingHolder.getTypesOfLaunch()) {
                option {
                    value = typeOfLaunch.getCodeName()
                    text(typeOfLaunch.getName())
                }
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