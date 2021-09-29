package ru.testing.utils

import java.nio.file.Path
import java.util.*
import kotlin.io.path.createDirectories

class TestingConfiguration {
    companion object {
        private val properties = Properties().apply {
            load(TestingConfiguration::class.java.getResourceAsStream("/testing.properties"))
        }

        // TODO make better. let, seriously?
        val DEPLOY_DIRECTORY: Path = Path.of(properties.getProperty("DEPLOY_DIRECTORY")).let {
            it.createDirectories()
            it
        }
    }
}