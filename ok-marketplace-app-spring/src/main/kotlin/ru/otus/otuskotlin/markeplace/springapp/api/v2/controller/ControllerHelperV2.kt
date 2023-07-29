package ru.otus.otuskotlin.markeplace.springapp.api.v2.controller

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.otus.otuskotlin.marketplace.api.logs.mapper.toLog
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.process
import ru.otus.otuskotlin.marketplace.logging.common.IMpLogWrapper
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> processV2(
    appSettings: MkplAppSettings,
    requestString: String,
    logger: IMpLogWrapper,
    logId: String,
): String = appSettings.processor.process(logger, logId,
    fromTransport = {
        val request = apiV2Mapper.decodeFromString<Q>(requestString)
        fromTransport(request)
    },
    sendResponse = {  apiV2Mapper.encodeToString(toTransportAd()) },
    toLog = { toLog("spring") }
)
