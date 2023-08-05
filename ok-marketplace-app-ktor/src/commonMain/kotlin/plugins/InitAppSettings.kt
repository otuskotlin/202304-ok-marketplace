package ru.otus.otuskotlin.marketplace.app.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.app.common.LoggerType
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.getLoggerProviderConf
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings

fun Application.initAppSettings(): MkplAppSettings {
    val loggerProvider = getLoggerProviderConf(getLoggerType())
    val corSettings = MkplCorSettings(
        loggerProvider = loggerProvider,
    )
    return MkplAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        processor = MkplAdProcessor(corSettings),
        logger = loggerProvider
    )
}

private fun Application.getLoggerType(): LoggerType =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "kmp" -> LoggerType.KMP
        "kermit" -> LoggerType.KERMIT
        "logback", null -> LoggerType.LOGBACK
        else -> throw Exception("Logger $mode is not allowed. Admitted values are kmp and logback")
    }
