package ru.otus.otuskotlin.marketplace.app.v1

import io.ktor.server.application.*
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersResponse
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.v2.offersAd

suspend fun ApplicationCall.offersAd(appSettings: MkplAppSettings) : Unit =
    processV1<AdOffersRequest, AdOffersResponse>(appSettings, ApplicationCall::offersAd::class, "ad-offers")
