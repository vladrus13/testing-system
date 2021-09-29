package ru.testing.limits

class Limits(
    var timeLimitMilliseconds: Long = 1000L,
    val memoryLimitBytes: Long = 256 * 1024L
) {
    companion object {
        val COMPILATION_LIMITS = Limits(
            timeLimitMilliseconds = 5000L
        )

        val OLYMPIC_LIMITS = Limits()
    }
}