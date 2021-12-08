import interfaces.AbstractExecutors
import interfaces.AbstractResultHolder
import interfaces.AbstractTestingQueue
import ru.testing.testlib.task.Task

class SimpleEnvironmentConfiguration(
    override val tasksHolder: MutableMap<Long, Task>,
    override val testingQueue: AbstractTestingQueue,
    override val executors: AbstractExecutors,
    override val resultHolder: AbstractResultHolder,
    startExecutors: Boolean = true,
) : EnvironmentConfiguration {

    init {
        if (startExecutors) {
            executors.run(this)
        }
    }
}