package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import ru.otus.otuskotlin.marketplace.api.logs.mapper.toLog
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import ru.otus.otuskotlin.marketplace.app.common.process
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.logging.common.IMpLogWrapper
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd

suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.processV2(
    processor: MkplAdProcessor,
    request: Q,
    logger: IMpLogWrapper,
    logId: String,
) {
    processor.process(logger, logId,
        fromTransport = { fromTransport(request) },
        sendResponse = { respond(apiV2Mapper.encodeToString(toTransportAd())) },
        toLog = { toLog(logId) },
    )
}
