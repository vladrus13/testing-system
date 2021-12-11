package ru.testing.databese

import interfaces.AbstractDatabaseInitializer
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.testing.databese.definition.Users

class DatabaseInitializer : AbstractDatabaseInitializer {
    override fun createSchema() {
        // TODO: make it nicer, build from properties
        Database.connect(
            "jdbc:postgresql://localhost:5432/testsys",
            driver = "org.postgresql.Driver",
            user = "vlad",
            password = "vlad"
        )
        transaction {
            SchemaUtils.create(Users)
        }
    }
}