package ru.otus.otuskotlin.marketplace.app.rabbit.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory

var rabbitLogger: Logger = LoggerFactory.getLogger("RabbitLogger")
data class RabbitConfig(
    val host: String = HOST,
    val port: Int = PORT,
    val user: String = RABBIT_USER,
    val password: String = RABBIT_PASSWORD
) {
    companion object {
        const val HOST = "localhost"
        const val PORT = 5672
        const val RABBIT_USER = "guest"
        const val RABBIT_PASSWORD = "guest"
    }
}
