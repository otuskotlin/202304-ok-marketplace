package ru.otus.otuskotlin.markeplace.springapp.models

import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider

data class MkplAppSettings(
    val appUrls: List<String> = emptyList(),
    val processor: MkplAdProcessor = MkplAdProcessor(),
    val logger: MpLoggerProvider
)
