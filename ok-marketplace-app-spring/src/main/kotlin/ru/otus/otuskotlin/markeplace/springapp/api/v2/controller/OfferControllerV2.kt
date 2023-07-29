package ru.otus.otuskotlin.markeplace.springapp.api.v2.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.markeplace.springapp.api.v1.controller.AdController
import ru.otus.otuskotlin.marketplace.api.v2.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.AdOffersResponse
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings

@RestController
@RequestMapping("v2/ad")
class OfferControllerV2(
    private val appSettings: MkplAppSettings
) {
    private val logger = appSettings.logger.logger(AdController::class)

    @PostMapping("offers")
    suspend fun searchOffers(@RequestBody request: String): String =
        processV2<AdOffersRequest, AdOffersResponse>(appSettings, request, logger, "ad-offers")
}

