package ru.testing.testing.task

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
    }

    class RE(private val code: Int, private val verdict: String) : TestVerdict() {
        override fun toString(): String {
            return "Code: $code. $verdict"
        }

    }

    class CE(private val verdict: String) : TestVerdict() {
        override fun toString(): String {
            return verdict
        }
    }

    class NL : TestVerdict() {
        override fun toString(): String {
            return "Not launched"
        }

    }

    abstract override fun toString(): String
}