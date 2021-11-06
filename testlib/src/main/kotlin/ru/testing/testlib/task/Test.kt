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
     * @param participant_answer answer of participant solution
     * @return verdict of checking
     */
    abstract fun verdict(participant_answer: String): TestVerdict
}
