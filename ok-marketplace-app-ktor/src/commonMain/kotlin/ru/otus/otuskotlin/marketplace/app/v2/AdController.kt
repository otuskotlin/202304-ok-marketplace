package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.MkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v2.*
import kotlin.reflect.KClass

private val clazzCreate: KClass<*> = ApplicationCall::createAd::class
suspend fun ApplicationCall.createAd(appSettings: MkplAppSettings) = processV2<AdCreateRequest, AdCreateResponse>(
    processor = appSettings.processor,
    request = receive<AdCreateRequest>(),
    logger = appSettings.logger.logger(clazzCreate),
    logId = "create",
)

private val clazzRead: KClass<*> = ApplicationCall::readAd::class
suspend fun ApplicationCall.readAd(appSettings: MkplAppSettings) = processV2<AdReadRequest, AdReadResponse>(
    processor = appSettings.processor,
    request = receive<AdReadRequest>(),
    logger = appSettings.logger.logger(clazzRead),
    logId = "read",
)

suspend fun ApplicationCall.updateAd(processor: MkplAdProcessor) {
    val request = receive<AdUpdateRequest>()
    val context = MkplContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportUpdate())
}

suspend fun ApplicationCall.deleteAd(processor: MkplAdProcessor) {
    val request = receive<AdDeleteRequest>()
    val context = MkplContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportDelete())
}

suspend fun ApplicationCall.searchAd(processor: MkplAdProcessor) {
    val request = receive<AdSearchRequest>()
    val context = MkplContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportSearch())
}
