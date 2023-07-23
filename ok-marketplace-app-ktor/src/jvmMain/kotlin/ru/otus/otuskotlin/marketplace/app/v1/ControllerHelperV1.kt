package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

import ru.otus.otuskotlin.marketplace.api.logs.mapper.toLog
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.IResponse

import ru.otus.otuskotlin.marketplace.app.common.process
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.logging.common.IMpLogWrapper
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd

suspend inline fun <reified Q : IRequest, reified R : IResponse> ApplicationCall.processV1(
    processor: MkplAdProcessor,
    request: Q,
    logger: IMpLogWrapper,
    logId: String,
) {
    processor.process(logger, logId,
        fromTransport = { fromTransport(request) },
        sendResponse = { respond(toTransportAd()) },
        toLog = { toLog(logId) },
    )
}
