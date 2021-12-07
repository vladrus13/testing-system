package ru.testing.testlib.task

import kotlinx.html.TABLE
import kotlinx.html.td
import kotlinx.html.tr

/**
 * Verdict we can get for a test
 *
 */
sealed class TestVerdict {
    /**
     * OK - correct answer
     */
    object OK : TestVerdict() {
        override fun toString(): String {
            return "OK!"
        }

        override fun toRow(index: Int, table: TABLE) {
            table.tr(classes = "ok_verdict") {
                td {
                    text("$index")
                }
                td {
                    text(OK.toString())
                }
            }
        }
    }

    /**
     * WA - wrong answer in the test
     *
     * @property description some additional information (like, "Different on line 99")
     */
    class WA(private val description: String) : TestVerdict() {
        override fun toString(): String {
            return "Wrong answer! $description"
        }

        override fun toRow(index: Int, table: TABLE) {
            table.tr(classes = "wa_verdict") {
                td {
                    text("$index")
                }
                td {
                    text(this@WA.toString())
                }
            }
        }
    }

    /**
     * TL - time limit in the test
     */
    object TL : TestVerdict() {
        override fun toString(): String = "Time limit."

        override fun toRow(index: Int, table: TABLE) {
            table.tr(classes = "tl_verdict") {
                td {
                    text("$index")
                }
                td {
                    text(this@TL.toString())
                }
            }
        }
    }

    /**
     * RE - Runtime error verdict on this test
     *
     * @property code
     * @property verdict
     */
    class RE(private val code: Int, private val verdict: String) : TestVerdict() {
        override fun toString(): String {
            return "Code: $code. $verdict"
        }

        override fun toRow(index: Int, table: TABLE) {
            table.tr(classes = "re_verdict") {
                td {
                    text("$index")
                }
                td {
                    text(this@RE.toString())
                }
            }
        }

    }

    /**
     * NL - not launcher on this test
     *
     */
    object NL : TestVerdict() {
        override fun toString(): String = "Not launched"

        override fun toRow(index: Int, table: TABLE) {
            table.tr(classes = "nl_verdict") {
                td {
                    text("$index")
                }
                td {
                    text(this@NL.toString())
                }
            }
        }
    }

    abstract override fun toString(): String

    /**
     * Adds verdict info to the table
     *
     * @param index index of the test
     * @param table the table
     */
    abstract fun toRow(index: Int, table: TABLE)
}