package ru.testing.tasks.holder

import ru.testing.testlib.limits.Limits
import ru.testing.testlib.task.CompileRunTask
import ru.testing.testlib.task.Test
import ru.testing.testlib.task.TestVerdict

/**
 * Test to A+B task
 *
 * @property a a
 * @property b b
 */
class APlusBTestPlusCTest(private val a: Int, private val b: Int, private val c: Int) : Test("$a $b $c\n") {
    override fun verdict(participant_answer: String): TestVerdict {
        return if (participant_answer.trim().toInt() == a + b + c) {
            TestVerdict.OK
        } else {
            TestVerdict.WA("Wrong answer. Expected: ${a + b + c}, actual: $participant_answer")
        }
    }
}

/**
 * A+B task
 *
 */
class APlusBPlusC : CompileRunTask(
    2,
    listOf(
        APlusBTestPlusCTest(0, 0, 0),
        APlusBTestPlusCTest(1, 0, 0),
        APlusBTestPlusCTest(0, 1, 1),
        APlusBTestPlusCTest(2, 2, 4),
        APlusBTestPlusCTest(20, 20, 40),
        APlusBTestPlusCTest(200, 200, 400),
    ),
    Limits.COMPILATION_LIMITS,
    Limits.OLYMPIC_LIMITS
)