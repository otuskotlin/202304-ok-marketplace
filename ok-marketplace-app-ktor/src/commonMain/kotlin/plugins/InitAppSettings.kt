package ru.otus.otuskotlin.marketplace.app.plugins

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider

fun Application.initAppSettings(): MkplAppSettings {
    val corSettings = MkplCorSettings(
        loggerProvider = getLoggerProviderConf(),
    )
    return MkplAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        processor = MkplAdProcessor(corSettings),
        logger = getLoggerProviderConf(),
    )
}
expect fun Application.getLoggerProviderConf(): MpLoggerProvider
