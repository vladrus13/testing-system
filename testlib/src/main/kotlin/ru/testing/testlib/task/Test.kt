package ru.testing.testlib.task

interface Test {

    fun getInput(): String

    /**
     * Check author and participant solution
     *
     * @param participantAnswer answer of participant's solution
     * @return verdict of checking
     */
    fun verdict(participantAnswer: String): TestVerdict
}