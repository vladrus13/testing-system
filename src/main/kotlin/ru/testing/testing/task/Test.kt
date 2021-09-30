package ru.testing.testing.task

abstract class Test(val input: String) {
    abstract fun verdict(participant_answer: String): TestVerdict
}
