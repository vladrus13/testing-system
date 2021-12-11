package ru.testing.testlib.task

interface Checker {
    fun check(participantAnswer: String, juryAnswer: String): TestVerdict
}