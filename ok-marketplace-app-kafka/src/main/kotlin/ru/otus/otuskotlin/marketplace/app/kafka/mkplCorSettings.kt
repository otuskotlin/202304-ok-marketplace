package ru.otus.otuskotlin.marketplace.app.kafka

import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.jvm.mpLoggerLogback

private val loggerProvider = MpLoggerProvider { mpLoggerLogback(it) }

val corSettings = MkplAppSettings(
    logger = loggerProvider
)