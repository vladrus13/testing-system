package ru.testing.polygon.database.definition

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 25).uniqueIndex()
    val passwordHash = varchar("password_hash", length = 72)  // 72 - the length of bcrypt hash
    override val primaryKey = PrimaryKey(id)
}
