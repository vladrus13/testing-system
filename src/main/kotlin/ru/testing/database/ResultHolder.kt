package ru.testing.database

import ru.testing.testing.task.TestVerdict
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class ResultHolder {
    companion object {
        val holder: ConcurrentMap<Long, List<TestVerdict>> = ConcurrentHashMap()
    }
}