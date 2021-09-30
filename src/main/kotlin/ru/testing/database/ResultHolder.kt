package ru.testing.database

import ru.testing.testing.task.TestVerdict
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * Result holder. Temporary solution. Will be replaced by database
 *
 */
class ResultHolder {
    companion object {
        /**
         * Holds all results by it id
         */
        val holder: ConcurrentMap<Long, List<TestVerdict>> = ConcurrentHashMap()
    }
}