package ru.otus.otuskotlin.markeplace.springapp.api.v1.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdOffersResponse
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings

@RestController
@RequestMapping("v1/ad")
class OfferController(
    private val appSettings: MkplAppSettings
) {
    private val logger by lazy { appSettings.logger.logger(AdController::class) }

    @PostMapping("offers")
    suspend fun searchOffers(@RequestBody request: AdOffersRequest): AdOffersResponse =
        processV1(appSettings, request, logger, "ad-offers")
}
