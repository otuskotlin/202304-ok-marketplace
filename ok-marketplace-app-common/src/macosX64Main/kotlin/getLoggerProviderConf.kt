package ru.otus.otuskotlin.marketplace.app.common

import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.kermit.mpLoggerKermit

actual fun getLoggerProviderConf(loggerType: LoggerType): MpLoggerProvider = MpLoggerProvider {
    mpLoggerKermit(it)
}

