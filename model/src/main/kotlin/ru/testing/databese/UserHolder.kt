package ru.testing.databese

import interfaces.AbstractUserHolder
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.testing.databese.definition.Users
import ru.testing.testlib.domain.User

class UserHolder : AbstractUserHolder {
    override fun findUserById(id: Int): User? {
        return transaction {
            Users.select { Users.id eq id }.map { mapToUser(it) }.firstOrNull()
        }
    }

    override fun findUserByName(name: String): User? {
        return transaction {
            Users.select { Users.name eq name }.map { mapToUser(it) }.firstOrNull()
        }
    }

    override fun createUser(name: String, passwordHash: String) {
        if (findUserByName(name) != null) {
            throw RuntimeException("This username is occupied")  // TODO: probably, can be better, don't know Kotlin specifics :(
        }
        return transaction {
            Users.insert {
                it[Users.name] = name
                it[Users.passwordHash] = passwordHash
            }
        }
    }

    private fun mapToUser(it: ResultRow) = User(
        id = it[Users.id],
        name = it[Users.name],
        passwordHash = it[Users.passwordHash]
    )
}