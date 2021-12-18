package ru.testing.databese.definition

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Submissions : Table() {
    val id = long("id").autoIncrement()
    val userId = long("userId").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val taskName = varchar("taskName", length = 50)
    val testCount = integer("testCount")  // Allows partial testing, for example, pretests.
    val serializedStatus = varchar("serializedStatus", length = 4096);
    override val primaryKey = PrimaryKey(id)
}