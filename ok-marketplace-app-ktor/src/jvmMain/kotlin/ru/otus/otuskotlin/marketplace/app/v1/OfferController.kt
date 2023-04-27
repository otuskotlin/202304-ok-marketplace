package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportOffers

suspend fun ApplicationCall.offersAd(processor: MkplAdProcessor) {
    val request = receive<AdOffersRequest>()
    val context = MkplContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportOffers())
}
