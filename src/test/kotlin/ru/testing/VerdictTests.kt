package ru.testing

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import ru.testing.TestUtils.TestTask.*
import ru.testing.polygon.submission.CPP
import kotlin.time.ExperimentalTime

@ExperimentalTime
class VerdictTests {
    @Test
    fun correctVerdict() = runBlocking {
        TestUtils.runTask(task = `A + B`, language = CPP, source = """
            #include <iostream>
            
            int main() {
                int a, b;
                std::cin >> a >> b;
                std::cout << a + b;
            }
        """.trimIndent()).let { println(it) }
    }
}