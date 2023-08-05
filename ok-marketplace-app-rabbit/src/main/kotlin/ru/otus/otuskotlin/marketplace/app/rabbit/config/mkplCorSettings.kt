package ru.otus.otuskotlin.marketplace.app.rabbit.config

import ru.otus.otuskotlin.marketplace.app.common.LoggerType
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.getLoggerProviderConf

val corSettings = MkplAppSettings(
    logger = getLoggerProviderConf(LoggerType.LOGBACK)
)