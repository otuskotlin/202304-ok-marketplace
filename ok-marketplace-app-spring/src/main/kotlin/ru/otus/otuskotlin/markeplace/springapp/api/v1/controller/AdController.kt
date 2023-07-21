package ru.otus.otuskotlin.markeplace.springapp.api.v1.controller

import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.app.common.processV1
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v1.*

@RestController
@RequestMapping("v1/ad")
class AdController(
    private val appSettings: MkplAppSettings
) {

    @PostMapping("create")
    // Перевести на WebFlux
    fun createAd(@RequestBody request: AdCreateRequest): AdCreateResponse = runBlocking {
        processV1<AdCreateRequest, AdCreateResponse>(
            processor = appSettings.processor,
            request = request,
            logger = appSettings.logger.logger(this::class.qualifiedName ?: "create"),
            logId = "create",
        )
    }

    @PostMapping("read")
// Перевести на WebFlux
    fun readAd(@RequestBody request: AdReadRequest): AdReadResponse = runBlocking {
        val context = MkplContext()
        context.fromTransport(request)
        appSettings.processor.exec(context)
        context.toTransportRead()
    }

    @RequestMapping("update", method = [RequestMethod.POST])
// Перевести на WebFlux
    fun updateAd(@RequestBody request: AdUpdateRequest): AdUpdateResponse = runBlocking {
        val context = MkplContext()
        context.fromTransport(request)
        appSettings.processor.exec(context)
        context.toTransportUpdate()
    }

    @PostMapping("delete")
// Перевести на WebFlux
    fun deleteAd(@RequestBody request: AdDeleteRequest): AdDeleteResponse = runBlocking {
        val context = MkplContext()
        context.fromTransport(request)
        appSettings.processor.exec(context)
        context.toTransportDelete()
    }

    @PostMapping("search")
// Перевести на WebFlux
    fun searchAd(@RequestBody request: AdSearchRequest): AdSearchResponse = runBlocking {
        val context = MkplContext()
        context.fromTransport(request)
        appSettings.processor.exec(context)
        context.toTransportSearch()
    }
}
