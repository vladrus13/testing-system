package ru.testing.html.views

import kotlinx.html.*

internal fun BODY.loginView() {
    text("Log in to Testing System")
    form(
        action = "/login",
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
            submitInput { value = "Login" }
        }
        a("/register") {
            +"or register"
        }
    }
}