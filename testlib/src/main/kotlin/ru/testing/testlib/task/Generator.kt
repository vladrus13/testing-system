package ru.testing.testlib.task

interface Generator {
    /**
     * Get test for task. MUST give equals tests for equals lists
     *
     * @param input input list initiators
     * @return test
     */
    fun generate(input: List<Int>): TextTest
}