package ru.otus.otuskotlin.markeplace.springapp.api.v2.controller

import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.*
import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.mappers.v2.*

@RestController
@RequestMapping("v2/ad")
class AdControllerV2(private val appSettings: MkplAppSettings) {

    @PostMapping("create")
    fun createAd(@RequestBody request: AdCreateRequest): AdCreateResponse = runBlocking {
        val context = MkplContext()
        context.fromTransport(request)
        appSettings.processor.exec(context)
        context.toTransportCreate()
    }

    @PostMapping("read")
    fun readAd(@RequestBody request: AdReadRequest): AdReadResponse = runBlocking {
        val context = MkplContext()
        context.fromTransport(request)
        appSettings.processor.exec(context)
        context.toTransportRead()
    }

    @PostMapping("update")
    fun updateAd(@RequestBody request: AdUpdateRequest): AdUpdateResponse = runBlocking {
        val context = MkplContext()
        context.fromTransport(request)
        appSettings.processor.exec(context)
        context.toTransportUpdate()
    }

    @PostMapping("delete")
    fun deleteAd(@RequestBody request: AdDeleteRequest): AdDeleteResponse = runBlocking {
        val context = MkplContext()
        context.fromTransport(request)
        appSettings.processor.exec(context)
        context.toTransportDelete()
    }

    @PostMapping("search")
    fun searchAd(@RequestBody request: AdSearchRequest): AdSearchResponse = runBlocking {
        val context = MkplContext()
        context.fromTransport(request)
        appSettings.processor.exec(context)
        context.toTransportSearch()
    }
}
