package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.server.application.*
import io.ktor.server.request.*
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersResponse
import ru.otus.otuskotlin.marketplace.app.MkplAppSettings
import kotlin.reflect.KClass

private val clazzOffers: KClass<*> = ApplicationCall::offersAd::class
suspend fun ApplicationCall.offersAd(appSettings: MkplAppSettings) = processV1<AdOffersRequest, AdOffersResponse>(
    processor = appSettings.processor,
    request = receive<AdOffersRequest>(),
    logger = appSettings.logger.logger(clazzOffers),
    logId = "offers",
)
