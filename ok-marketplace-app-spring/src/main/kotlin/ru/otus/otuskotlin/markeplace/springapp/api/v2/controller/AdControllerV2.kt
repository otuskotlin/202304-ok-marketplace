package ru.otus.otuskotlin.markeplace.springapp.api.v2.controller

import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.markeplace.springapp.api.v1.controller.AdController
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.mappers.v2.*

@RestController
@RequestMapping("v2/ad")
class AdControllerV2(
    private val appSettings: MkplAppSettings
) {
    private val logger = appSettings.logger.logger(AdController::class)

    @PostMapping("create")
    suspend fun createAd(@RequestBody request: String): String =
        processV2<AdCreateRequest, AdCreateResponse>(appSettings, request, logger, "ad-create")

    @PostMapping("read")
    suspend fun readAd(@RequestBody request: String): String =
        processV2<AdReadRequest, AdCreateResponse>(appSettings, request, logger, "ad-create")

    @PostMapping("update")
    suspend fun updateAd(@RequestBody request: String): String =
        processV2<AdUpdateRequest, AdUpdateResponse>(appSettings, request, logger, "ad-update")

    @PostMapping("delete")
    suspend fun deleteAd(@RequestBody request: String): String =
        processV2<AdDeleteRequest, AdDeleteResponse>(appSettings, request, logger, "ad-delete")

    @PostMapping("search")
    suspend fun searchAd(@RequestBody request: String): String =
        processV2<AdSearchRequest, AdSearchResponse>(appSettings,  request, logger, "ad-search")
}
