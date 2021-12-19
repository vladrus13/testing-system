package ru.testing

import interfaces.AbstractUserHolder
import ru.testing.testlib.domain.User

class InMemoryUserHolder: AbstractUserHolder {
    private val id2user: MutableMap<Long, User> = mutableMapOf()
    private val name2user: MutableMap<String, User> = mutableMapOf()
    private var id = 1L
    override fun findUserById(id: Long): User? = id2user[id]

    override fun findUserByName(name: String): User? = name2user[name]

    override fun createUser(name: String, passwordHash: String) {
        val curId = id++
        val user = User(curId, name, passwordHash)
        id2user[curId] = user
        name2user[name] = user
    }
}