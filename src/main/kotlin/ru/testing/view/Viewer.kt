package ru.testing.view

import kotlinx.html.*

/**
 * Viewer part of MVC model. Contain functions to return answers to user
 *
 */
class Viewer {
    companion object {

        /**
         * Return full body answer to user
         *
         * @param html html which will contain answer
         * @param head head additional info
         * @param body html additional info
         */
        fun getHTML(html: HTML, head: HEAD.() -> Unit = { }, body: BODY.() -> Unit = { }) {
            html.head {
                link(rel = "stylesheet", href = "/styles.css", type = "text/css")
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