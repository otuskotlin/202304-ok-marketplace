package ru.otus.otuskotlin.marketplace.app.common

import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider

data class MkplAppSettings(
    val appUrls: List<String> = emptyList(),
    val corSettings: MkplCorSettings = MkplCorSettings(),
    val processor: MkplAdProcessor = MkplAdProcessor(settings = corSettings),
    val logger: MpLoggerProvider = MpLoggerProvider()
)