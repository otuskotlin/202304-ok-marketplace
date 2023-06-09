package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportOffers

suspend fun ApplicationCall.offersAd(processor: MkplAdProcessor) {
    val request = apiV2Mapper.decodeFromString<AdOffersRequest>(receiveText())
    val context = MkplContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(apiV2Mapper.encodeToString(context.toTransportOffers()))
}
