package ru.testing.testing.task

import ru.testing.testing.limits.Limits

class APlusBTest(val a: Int, val b: Int) : Test("$a $b\n") {
    override fun verdict(participant_answer: String): TestVerdict {
        return if (participant_answer.toInt() == a + b) {
            TestVerdict.OK
        } else {
            TestVerdict.WA("Wrong answer. Expected: ${a + b}, actual: $participant_answer")
        }
    }
}

class APlusB : Task(
    listOf(
        APlusBTest(0, 0),
        APlusBTest(1, 0),
        APlusBTest(0, 1),
        APlusBTest(2, 2),
    ), Limits.OLYMPIC_LIMITS
)