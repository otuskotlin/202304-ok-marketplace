package ru.otus.otuskotlin.marketplace.app.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.jvm.mpLoggerLogback
import ru.otus.otuskotlin.marketplace.logging.kermit.mpLoggerKermit

actual fun Application.getLoggerProviderConf(): MpLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "kmp", "keremit" -> MpLoggerProvider { mpLoggerKermit(it) }
        "logback", null -> MpLoggerProvider { mpLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Admitted values are kmp and logback")
}
