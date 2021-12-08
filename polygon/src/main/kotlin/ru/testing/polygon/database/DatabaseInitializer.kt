package ru.testing.polygon.database

import interfaces.AbstractDatabaseInitializer
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.testing.polygon.database.definition.Users

class DatabaseInitializer : AbstractDatabaseInitializer {
    override fun createSchema() {
        // TODO: make it nicer, build from properties
        Database.connect("jdbc:postgresql://localhost:5432/testsys", driver = "org.postgresql.Driver", user = "sagolbah", password = "")
        transaction {
            SchemaUtils.create(Users)
        }
    }
}