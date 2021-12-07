package ru.testing.testlib.task

/**
 * One test from the task
 *
 * @property input input we give to participant solution
 */
abstract class Test(val input: String) {
    /**
     * Check author and participant solution
     *
     * @param participantAnswer answer of participant's solution
     * @return verdict of checking
     */
    abstract fun verdict(participantAnswer: String): TestVerdict
}
