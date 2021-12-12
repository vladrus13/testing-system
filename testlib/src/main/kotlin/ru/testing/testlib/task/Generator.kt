package ru.testing.testlib.task

/**
 * Test generator for tasks
 *
 */
interface Generator {
    /**
     * Get test for task. MUST give equals tests for equals lists
     *
     * @param input input list initiators
     * @return test
     */
    fun generate(input: List<Int>): TextTest
}