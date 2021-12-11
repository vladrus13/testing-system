package ru.testing.testlib.task

/**
 * Class which checks participant answer to correctness
 *
 */
interface Checker {
    /**
     * Checks participant answer and jury answer
     *
     * @param input input text
     * @param participantAnswer participant answer
     * @param juryAnswer jury answer
     * @return
     */
    fun check(input: String, participantAnswer: String, juryAnswer: String): TestVerdict
}