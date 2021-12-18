package ru.testing.databese

import interfaces.AbstractDatabaseInitializer
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.testing.databese.definition.Submissions
import ru.testing.databese.definition.TestVerdicts
import ru.testing.databese.definition.Users
import java.io.IOException
import java.util.*

class DatabaseInitializer : AbstractDatabaseInitializer {
    override fun createSchema() {
        val properties = Properties()
        try {
            properties.load(DatabaseInitializer::class.java.getResourceAsStream("/database.properties"))
        } catch (e: IOException) {
            // TODO make normal
        } catch (e: NullPointerException) {
            // TODO make normal
        }

        Database.connect(
            url = properties.getProperty("url"),
            driver = properties.getProperty("driver"),
            user = properties.getProperty("user"),
            password = properties.getProperty("password")
        )
        transaction {
            SchemaUtils.create(Users, Submissions, TestVerdicts)
        }
    }
}
