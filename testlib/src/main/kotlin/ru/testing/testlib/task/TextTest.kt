package ru.testing.testlib.task

/**
 * One test from the task
 *
 * @property input input we give to participant solution
 */
abstract class TextTest(
    private val input: String,
    protected val juryAnswer: String,
    private val checker: Checker? = null
) : Test {
    override fun getInput(): String = input

    /**
     * Check author and participant solution. Must be implemented if checker null
     *
     * @param participantAnswer answer of participant's solution
     * @return verdict of checking
     */
    open fun check(participantAnswer: String): TestVerdict {
        throw UnsupportedOperationException()
    }

    override fun verdict(participantAnswer: String): TestVerdict {
        if (checker != null) {
            return checker.check(participantAnswer, juryAnswer)
        }
        return check(participantAnswer)
    }
}
