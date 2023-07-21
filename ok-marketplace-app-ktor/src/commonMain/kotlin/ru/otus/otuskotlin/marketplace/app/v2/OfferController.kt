package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import io.ktor.server.request.*
import ru.otus.otuskotlin.marketplace.api.v2.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.AdOffersResponse
import ru.otus.otuskotlin.marketplace.app.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.processV2
import kotlin.reflect.KClass

private val clazzOffers: KClass<*> = ApplicationCall::offersAd::class
suspend fun ApplicationCall.offersAd(appSettings: MkplAppSettings) = processV2<AdOffersRequest, AdOffersResponse>(
    processor = appSettings.processor,
    request = receive<AdOffersRequest>(),
    logger = appSettings.logger.logger(clazzOffers),
    logId = "offers",
)
