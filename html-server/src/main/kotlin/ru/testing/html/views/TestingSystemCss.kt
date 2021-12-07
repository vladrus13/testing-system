package ru.testing.html.views

import kotlinx.css.*

internal fun CssBuilder.testingSystemCss() {
    rule(".verdicts .ok_verdict") {
        backgroundColor = rgb(216, 256, 216)
    }
    rule(".verdicts .wa_verdict") {
        backgroundColor = rgb(256, 216, 216)
    }
    rule(".verdicts .tl_verdict") {
        backgroundColor = rgb(216, 216, 256)
    }
    rule(".verdicts .re_verdict") {
        backgroundColor = rgb(256, 256, 216)
    }
    rule(".verdicts") {
        border = "1px"
        borderStyle = BorderStyle.solid
        borderColor = Color.black
    }
}