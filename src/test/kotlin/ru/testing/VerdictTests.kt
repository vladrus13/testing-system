package ru.testing

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import ru.testing.TestUtils.TestTask.`A + B + C`
import ru.testing.TestUtils.TestTask.`A + B`
import ru.testing.TestUtils.runTask
import ru.testing.VerdictTests.CorrectPrograms.correctCpp
import ru.testing.VerdictTests.CorrectPrograms.correctJava
import ru.testing.polygon.submission.OlympiadCpp
import ru.testing.polygon.submission.OlympiadJava
import ru.testing.testlib.task.SubmissionVerdict
import ru.testing.testlib.task.TestVerdict
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Execution(ExecutionMode.CONCURRENT)
class VerdictTests {
    private object CorrectPrograms {
        fun correctCpp(variables: Int) = """
            #include <iostream>

            int main() {
                int ${(1..variables).joinToString(", ") { "a$it" }};
                std::cin >> ${(1..variables).joinToString(" >> ") { "a$it" }};
                std::cout << ${(1..variables).joinToString(" + ") { "a$it" }};
            }
        """.trimIndent()

        fun correctJava(numbers: Int) = """
            import java.util.Scanner;

            public class Source {
                public static void main(String[] args) {
                    Scanner sc = new Scanner(System.in);
                    final int ${(1..numbers).joinToString(", ") { "a$it = sc.nextInt()" }};
                    sc.close();
                    System.out.println(${(1..numbers).joinToString(" + ") { "a$it" }});
                }
            }

        """.trimIndent()

    }

    @Nested
    inner class Ok {
        private fun SubmissionVerdict.allOk(expectedCount: Int) {
            Assertions.assertTrue(
                this is SubmissionVerdict.RunningVerdict
                        && this.tests.size == expectedCount
                        && this.tests.all { it == TestVerdict.OK },
                toString())
        }

        @Test
        fun cpp() = runBlocking {
            runTask(task = `A + B`, language = OlympiadCpp, source = correctCpp(2)).allOk(12)
            runTask(task = `A + B + C`, language = OlympiadCpp, source = correctCpp(3)).allOk(6)
        }

        @Test
        fun java() = runBlocking {
            runTask(task = `A + B`, language = OlympiadJava, source = correctJava(2)).allOk(12)
            runTask(task = `A + B + C`, language = OlympiadJava, source = correctJava(3)).allOk(6)
        }
    }

    @Nested
    inner class CompilationError {
        @Test
        fun cpp() = runBlocking {
            runTask(
                task = `A + B`, language = OlympiadCpp,
                source = correctCpp(2).replace("iostream", "lalala")
            ).assertCorrectness()
        }

        @Test
        fun java() = runBlocking {
            runTask(
                task = `A + B`, language = OlympiadJava,
                source = correctJava(2).replace("class", "lalala")
            ).assertCorrectness()
        }

        private fun SubmissionVerdict.assertCorrectness() {
            Assertions.assertTrue(this is SubmissionVerdict.CompilationError, toString())
        }
    }

    @Nested
    inner class TimeLimit {
        private fun makeTL(code: String) = code.replaceFirst("}", "while (true) {}}").also { println(it) }

        @Test
        fun cpp() = runBlocking {
            runTask(task = `A + B`, language = OlympiadCpp, source = makeTL(correctCpp(2))).assertCorrectness()
        }

        @Test
        fun java() = runBlocking {
            runTask(task = `A + B`, language = OlympiadJava, source = makeTL(correctJava(2))).assertCorrectness()
        }

        private fun SubmissionVerdict.assertCorrectness() {
            Assertions.assertTrue(
                this is SubmissionVerdict.RunningVerdict && tests.all { it is TestVerdict.TL }, toString()
            )
        }
    }

    @Nested
    inner class RuntimeError {
        private fun makeRE(code: String) = code.replace("+", "+ 2 / (a1 - a1) +")

        @Test
        fun cpp() = runBlocking {
            runTask(task = `A + B`, language = OlympiadCpp, source = makeRE(correctCpp(2))).assertCorrectness()
        }

        @Test
        fun java() = runBlocking {
            runTask(task = `A + B`, language = OlympiadJava, source = makeRE(correctJava(2))).assertCorrectness()
        }

        private fun SubmissionVerdict.assertCorrectness() {
            Assertions.assertTrue(
                this is SubmissionVerdict.RunningVerdict && tests.all { it is TestVerdict.RE }, toString()
            )
        }
    }

    @Nested
    inner class WrongAnswer {
        private fun makeWA(code: String) = code.replace("+", "+ 1 +")

        @Test
        fun cpp() = runBlocking {
            runTask(task = `A + B`, language = OlympiadCpp, source = makeWA(correctCpp(2))).assertCorrectness()
        }

        @Test
        fun java() = runBlocking {
            runTask(task = `A + B`, language = OlympiadJava, source = makeWA(correctJava(2))).assertCorrectness()
        }

        private fun SubmissionVerdict.assertCorrectness() {
            Assertions.assertTrue(
                this is SubmissionVerdict.RunningVerdict && tests.all { it is TestVerdict.WA }, toString()
            )
        }
    }
}