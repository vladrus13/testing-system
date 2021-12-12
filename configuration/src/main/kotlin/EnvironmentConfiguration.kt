import interfaces.*
import ru.testing.testlib.task.Task

interface EnvironmentConfiguration {
    val tasksHolder: MutableMap<Long, Task>
    val testingQueue: AbstractTestingQueue
    val executors: AbstractExecutors
    val resultHolder: AbstractResultHolder
    val userHolder: AbstractUserHolder
    val databaseInitializer: AbstractDatabaseInitializer
    val typeOfLaunchingHolder: AbstractTypeOfLaunchingHolder
}