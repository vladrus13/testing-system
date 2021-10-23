package ru.testing.view

import kotlinx.html.*

class Viewer {
    companion object {
        fun getHTML(html: HTML, head: HEAD.() -> Unit = { }, body: BODY.() -> Unit = { }) {
            html.head {
                head(this)
            }
            html.body {
                h1 {
                    text("Testing system!")
                }
                body(this)
            }
        }
    }
}