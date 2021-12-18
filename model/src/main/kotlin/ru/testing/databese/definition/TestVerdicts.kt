package ru.testing.databese.definition

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TestVerdicts : Table() {
    val testId = integer("testId")
    val submissionId = long("submissionId").references(Submissions.id, onDelete = ReferenceOption.CASCADE)
    val serializedStatus = varchar("serializedStatus", length = 16384)
    override val primaryKey = PrimaryKey(testId, submissionId)
}
