package ru.testing.tasks.holder

import ru.testing.testlib.limits.Limits
import ru.testing.testlib.task.*
import kotlin.random.Random

/**
 * Test to A+B task
 *
 * @property a a
 * @property b b
 */
class APlusBTextTest(private val a: Int, private val b: Int) : TextTest("$a $b\n", (a + b).toString()) {
    override fun verdict(participantAnswer: String): TestVerdict {
        return if (participantAnswer.trim().toInt() == juryAnswer.toInt()) {
            TestVerdict.OK
        } else {
            TestVerdict.WA("Wrong answer. Expected: ${a + b}, actual: $participantAnswer")
        }
    }
}

/**
 * Checker of absolute answers equals
 *
 */
class AbsoluteEqualsChecker : Checker {
    override fun check(input: String, participantAnswer: String, juryAnswer: String): TestVerdict {
        return if (participantAnswer.trim() == juryAnswer) {
            TestVerdict.OK
        } else {
            TestVerdict.WA("Wrong answer. Expected: $juryAnswer, actual: $participantAnswer")
        }
    }

}

/**
 * Test to task A + B with checker
 *
 * @param a
 * @param b
 */
class APlusBTest(a: Int, b: Int) :
    TextTest("$a $b", (a + b).toString(), AbsoluteEqualsChecker())

/**
 * Generator implementation to task A + B
 *
 */
class APlusBGenerator : Generator {
    override fun generate(input: List<Int>): TextTest {
        val from = input[0]
        val until = input[1]
        val seed = input[2]
        return APlusBTest(
            Random(seed).nextInt(0, until - from) + from,
            Random(seed).nextInt(0, until - from) + from
        )
    }

}

/**
 * A + B task
 *
 */
class APlusB : CompileRunTask(
    listOf(
        APlusBTextTest(0, 0),
        APlusBTextTest(1, 0),
        APlusBTextTest(0, 1),
        APlusBTextTest(2, 2),
        APlusBGenerator().generate(listOf(-10, 10, 0)),
        APlusBGenerator().generate(listOf(-100, 100, 0)),
        APlusBGenerator().generate(listOf(-1000, 1000, 0)),
        APlusBGenerator().generate(listOf(-10000, 10000, 0)),
        APlusBGenerator().generate(listOf(-100000, 100000, 0)),
        APlusBGenerator().generate(listOf(-1000000, 1000000, 0)),
        APlusBGenerator().generate(listOf(-10000000, 10000000, 0)),
        APlusBGenerator().generate(listOf(-100000000, 100000000, 0)),
    ),
    Limits.COMPILATION_LIMITS,
    Limits.OLYMPIC_LIMITS
)