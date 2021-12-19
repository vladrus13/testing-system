package ru.testing.html.views

import kotlinx.html.*

internal fun BODY.registerView() {
    text("Register")
    form(
        action = "/register",
        encType = FormEncType.applicationXWwwFormUrlEncoded,
        method = FormMethod.post
    ) {
        p {
            +"Username:"
            textInput(name = "username")
        }
        p {
            +"Password:"
            passwordInput(name = "password")
        }
        p {
            +"Confirm password:"
            passwordInput(name = "confPassword")
        }
        p {
            submitInput { value = "Register" }
        }
    }
}
