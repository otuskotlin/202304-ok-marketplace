package ru.otus.otuskotlin.marketplace.app.common

import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider

data class MkplAppSettings(
    val appUrls: List<String> = emptyList(),
    val processor: MkplAdProcessor = MkplAdProcessor(),
    val logger: MpLoggerProvider
)

enum class LoggerType {
    KMP,
    KERMIT,
    LOGBACK
}

expect fun getLoggerProviderConf(loggerType: LoggerType): MpLoggerProvider
