package ru.otus.otuskotlin.marketplace.blackbox.fixture.db

import mu.KotlinLogging
import java.sql.DriverManager

private val log = KotlinLogging.logger {}

class PostgresClearer : DbClearer {
    private var initialized = false
    private val connection by lazy {
        initialized = true
        DriverManager.getConnection("jdbc:postgresql://localhost/marketplace", "user", "password")
    }

    override fun clear() {
        connection.createStatement().use { statement ->
            val rows = statement.executeUpdate("delete from ad")
            log.info("    delete $rows row(s) from ad")
        }
    }

    override fun close() {
        if (initialized) connection.close()
    }
}