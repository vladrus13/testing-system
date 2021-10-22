package ru.testing.testing.task

import ru.testing.testing.limits.Limits

/**
 * Test to A+B task
 *
 * @property a a
 * @property b b
 */
class APlusBTest(private val a: Int, private val b: Int) : Test("$a $b\n") {
    override fun verdict(participant_answer: String): TestVerdict {
        return if (participant_answer.trim().toInt() == a + b) {
            TestVerdict.OK
        } else {
            TestVerdict.WA("Wrong answer. Expected: ${a + b}, actual: $participant_answer")
        }
    }
}

/**
 * A+B task
 *
 */
class APlusB : CompileRunTask(
    listOf(
        APlusBTest(0, 0),
        APlusBTest(1, 0),
        APlusBTest(0, 1),
        APlusBTest(2, 2),
    ),
    Limits.COMPILATION_LIMITS,
    Limits.OLYMPIC_LIMITS
)