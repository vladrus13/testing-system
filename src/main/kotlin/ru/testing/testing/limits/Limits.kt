package ru.testing.testing.limits

/**
 * Class for limits of execution of programs
 *
 * @property timeLimitMilliseconds how much program can work
 * @property memoryLimitBytes how much memory can program take
 */
class Limits(
    var timeLimitMilliseconds: Long = 1000L,
    val memoryLimitBytes: Long = 256 * 1024L
) {
    companion object {
        /**
         * On compilation limits
         */
        val COMPILATION_LIMITS = Limits(
            timeLimitMilliseconds = 5000L
        )

        /**
         * Classic olympiad limit
         */
        val OLYMPIC_LIMITS = Limits()
    }
}