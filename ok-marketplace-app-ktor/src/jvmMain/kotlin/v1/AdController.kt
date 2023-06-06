package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.marketplace.api.v1.models.AdCreateRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdCreateResponse
import ru.otus.otuskotlin.marketplace.api.v1.models.AdDeleteRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdReadRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdReadResponse
import ru.otus.otuskotlin.marketplace.api.v1.models.AdSearchRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdUpdateRequest
import ru.otus.otuskotlin.marketplace.app.MkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportDelete
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportSearch
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportUpdate
import kotlin.reflect.KClass

private val clazzCreate: KClass<*> = ApplicationCall::createAd::class
suspend fun ApplicationCall.createAd(appSettings: MkplAppSettings) = processV1<AdCreateRequest, AdCreateResponse>(
    processor = appSettings.processor,
    request = receive<AdCreateRequest>(),
    logger = appSettings.logger.logger(clazzCreate),
    logId = "create",
)

private val clazzRead: KClass<*> = ApplicationCall::readAd::class
suspend fun ApplicationCall.readAd(appSettings: MkplAppSettings)  = processV1<AdReadRequest, AdReadResponse>(
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
