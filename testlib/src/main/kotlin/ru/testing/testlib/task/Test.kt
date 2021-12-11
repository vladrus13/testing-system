package ru.testing.testlib.task

/**
 * One test to testing system
 *
 */
interface Test {
    /**
     * Get input from test
     *
     * @return input text
     */
    fun getInput(): String

    /**
     * Check author and participant solution
     *
     * @param participantAnswer answer of participant's solution
     * @return verdict of checking
     */
    fun verdict(participantAnswer: String): TestVerdict
}