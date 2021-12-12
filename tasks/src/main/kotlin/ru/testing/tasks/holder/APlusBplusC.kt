package ru.testing.tasks.holder

import ru.testing.testlib.limits.Limits
import ru.testing.testlib.task.CompileRunTask
import ru.testing.testlib.task.TestVerdict
import ru.testing.testlib.task.TextTest

/**
 * Test to A+B task
 *
 * @property a a
 * @property b b
 */
class APlusBTestPlusCTextTest(private val a: Int, private val b: Int, private val c: Int) :
    TextTest("$a $b $c\n", (a + b + c).toString()) {
    override fun verdict(participantAnswer: String): TestVerdict {
        return if (participantAnswer.trim().toInt() == a + b + c) {
            TestVerdict.OK
        } else {
            TestVerdict.WA("Wrong answer. Expected: ${a + b + c}, actual: $participantAnswer")
        }
    }
}

/**
 * A + B + C task
 *
 */
class APlusBPlusC : CompileRunTask(
    listOf(
        APlusBTestPlusCTextTest(0, 0, 0),
        APlusBTestPlusCTextTest(1, 0, 0),
        APlusBTestPlusCTextTest(0, 1, 1),
        APlusBTestPlusCTextTest(2, 2, 4),
        APlusBTestPlusCTextTest(20, 20, 40),
        APlusBTestPlusCTextTest(200, 200, 400),
    ),
    Limits.COMPILATION_LIMITS,
    Limits.OLYMPIC_LIMITS
)