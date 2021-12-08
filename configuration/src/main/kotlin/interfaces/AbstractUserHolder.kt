package interfaces

import ru.testing.testlib.domain.User

interface AbstractUserHolder {
    /**
     * Receives user by ID
     * @param id userid
     * @return user if it exists
     */
    fun findUserById(id: Int): User?

    /**
     * Receives user by its username
     * @param name username
     * @return user if it exists
     */
    fun findUserByName(name: String): User?

    /**
     * Attempts to create new user and save it into the database
     * @param name username
     * @param passwordHash password hash
     */
    fun createUser(name: String, passwordHash: String)
}