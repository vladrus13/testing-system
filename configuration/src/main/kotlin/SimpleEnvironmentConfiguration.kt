import interfaces.*
import ru.testing.testlib.task.Task

class SimpleEnvironmentConfiguration(
    override val tasksHolder: MutableMap<Long, Task>,
    override val testingQueue: AbstractTestingQueue,
    override val executors: AbstractExecutors,
    override val resultHolder: AbstractResultHolder,
    override val userHolder: AbstractUserHolder,
    override val databaseInitializer: AbstractDatabaseInitializer,
    startExecutors: Boolean = true,
) : EnvironmentConfiguration {

    init {
        databaseInitializer.createSchema()
        if (startExecutors) {
            executors.run(this)
        }
    }
}