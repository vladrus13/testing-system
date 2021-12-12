package interfaces

import EnvironmentConfiguration
import ru.testing.testlib.limits.Limits
import ru.testing.testlib.task.Task
import java.nio.file.Path

/**
 * Submission of participant. We must test it
 *
 */
interface AbstractTypeOfLaunching {

    /**
     * Get official name of programming language
     *
     * @return name
     */
    fun getName(): String

    /**
     * Get code name of programming language
     *
     * @return code name
     */
    fun getCodeName(): String

    /**
     * Output of execution
     *
     * @property output output of program
     * @property error error stream of program
     * @property code exiting code of program
     */
    class OutputProcessFile(
        val output: String = "",
        val error: String = "",
        val code: Int = 0
    )

    /**
     * Execute command
     *
     * @param command command what we execution
     * @param directory directory where we're executing
     * @param input input which we give to execution
     * @param limits limits of execution
     * @return
     */
    fun execute(
        command: List<String>,
        directory: Path,
        input: String? = null,
        limits: Limits
    ): OutputProcessFile?

    /**
     * Runa solver file on tests
     *
     * @param path path to solver
     * @param task task to test
     * @return list of received verdicts
     */
    fun runSolverFile(configuration: EnvironmentConfiguration, submissionId: Long, path: Path, task: Task)
}