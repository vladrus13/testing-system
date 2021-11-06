package ru.testing.testlib.task

import kotlinx.html.TABLE
import kotlinx.html.td
import kotlinx.html.tr

/**
 * Verdict we can get on testing
 *
 */
sealed class TestVerdict {
    /**
     * OK - correct test
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
     * WA - wrong answer on this test
     *
     * @property s some addition information (like, "Different on line 99")
     */
    class WA(private val s: String) : TestVerdict() {
        override fun toString(): String {
            return "Wrong answer! $s"
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
     * TL - time limit on this test
     *
     * @property time how much participant take (can be less than real)
     */
    class TL(private val time: Long) : TestVerdict() {
        override fun toString(): String {
            return "Time limit. Time: $time"
        }

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
    class NL : TestVerdict() {
        override fun toString(): String {
            return "Not launched"
        }

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
     * Add to table info about verdict
     *
     * @param index index of test
     * @param table table
     */
    abstract fun toRow(index: Int, table: TABLE)
}