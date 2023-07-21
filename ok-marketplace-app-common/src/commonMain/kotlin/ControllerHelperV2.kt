package ru.otus.otuskotlin.marketplace.app.common

import ru.otus.otuskotlin.marketplace.api.logs.mapper.toLog
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.logging.common.IMpLogWrapper
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd

suspend inline fun <reified Q : IRequest, reified R : IResponse> processV2(
    processor: MkplAdProcessor,
    request: Q,
    logger: IMpLogWrapper,
    logId: String,
): R  = processor.process(logger, logId,
        fromTransport = { fromTransport(request) },
        sendResponse = { toTransportAd() as R },
        toLog = { toLog(logId) },
)
