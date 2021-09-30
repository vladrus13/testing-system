package ru.testing.testing.task

sealed class TestVerdict {
    object OK : TestVerdict() {
        override fun toString(): String {
            return "OK!"
        }
    }

    class WA(val s: String) : TestVerdict() {
        override fun toString(): String {
            return "Wrong answer! $s"
        }
    }

    class TL(val time: Long) : TestVerdict() {
        override fun toString(): String {
            return "Time limit. Time: $time"
        }
    }

    abstract override fun toString(): String;
}