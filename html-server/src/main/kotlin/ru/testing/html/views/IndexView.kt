package ru.testing.html.views

import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.br
import kotlinx.html.h1
import ru.testing.html.views.utils.Viewer

internal fun HTML.indexView() {
    Viewer.getHTML(
        html = this,
        body = {
            h1 {
                text("Hello! This is a testing system!")
            }
            a("http://localhost:8080/chooseFile") {
                +"Choose File"
            }
            br
            a("/submissions") {
                +"My submissions"
            }
            br
            a("/logout") {
                +"Logout"
            }
        }
    )
}