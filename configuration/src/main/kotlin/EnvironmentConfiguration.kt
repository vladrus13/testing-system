import interfaces.AbstractExecutors
import interfaces.AbstractResultHolder
import interfaces.AbstractTestingQueue
import ru.testing.testlib.task.Task

interface EnvironmentConfiguration {
    val tasksHolder: MutableMap<Long, Task>
    val testingQueue: AbstractTestingQueue
    val executors: AbstractExecutors
    val resultHolder: AbstractResultHolder
}