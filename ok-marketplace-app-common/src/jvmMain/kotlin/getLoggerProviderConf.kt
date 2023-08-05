package ru.otus.otuskotlin.marketplace.app.common

import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.jvm.mpLoggerLogback
import ru.otus.otuskotlin.marketplace.logging.kermit.mpLoggerKermit

actual fun getLoggerProviderConf(loggerType: LoggerType): MpLoggerProvider =
    when (loggerType) {
        LoggerType.KMP, LoggerType.KERMIT -> MpLoggerProvider { mpLoggerKermit(it) }
        LoggerType.LOGBACK -> MpLoggerProvider { mpLoggerLogback(it) }
}
