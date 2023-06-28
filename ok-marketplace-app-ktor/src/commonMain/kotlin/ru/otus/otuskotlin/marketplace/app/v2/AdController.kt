package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v2.*

suspend fun ApplicationCall.createAd(processor: MkplAdProcessor) {
    val request = apiV2Mapper.decodeFromString<AdCreateRequest>(receiveText())
    val context = MkplContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(apiV2Mapper.encodeToString(context.toTransportCreate()))
}

suspend fun ApplicationCall.readAd(processor: MkplAdProcessor) {
    val request = apiV2Mapper.decodeFromString<AdReadRequest>(receiveText())
    val context = MkplContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(apiV2Mapper.encodeToString(context.toTransportRead()))
}

suspend fun ApplicationCall.updateAd(processor: MkplAdProcessor) {
    val request = apiV2Mapper.decodeFromString<AdUpdateRequest>(receiveText())
    val context = MkplContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(apiV2Mapper.encodeToString(context.toTransportUpdate()))
}

suspend fun ApplicationCall.deleteAd(processor: MkplAdProcessor) {
    val request = apiV2Mapper.decodeFromString<AdDeleteRequest>(receiveText())
    val context = MkplContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(apiV2Mapper.encodeToString(context.toTransportDelete()))
}

suspend fun ApplicationCall.searchAd(processor: MkplAdProcessor) {
    val request = apiV2Mapper.decodeFromString<AdSearchRequest>(receiveText())
    val context = MkplContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(apiV2Mapper.encodeToString(context.toTransportSearch()))
}
