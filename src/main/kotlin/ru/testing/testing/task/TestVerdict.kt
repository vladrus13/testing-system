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

    abstract override fun toString(): String
}