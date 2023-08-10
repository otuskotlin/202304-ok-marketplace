package ru.otus.otuskotlin.marketplace.app.repo

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

const val COMMON_REQUEST_ID = "12345"
class V2AdInmemoryApiTest {
    private val uuidOld = "10000000-0000-0000-0000-000000000001"


    private val createAd = AdCreateObject(
        title = "Болт",
        description = "КРУТЕЙШИЙ",
        adType = DealSide.DEMAND,
        visibility = AdVisibility.PUBLIC,
    )
    private val requestObj = AdCreateRequest(
        requestId = "12345",
        ad = createAd,
        debug = AdDebug(
            mode = AdRequestDebugMode.TEST,
        )
    )
    @Test
    fun create() = testApplication {
        val responseObj = initObject(client)
        assertEquals(createAd.title, responseObj.ad?.title)
        assertEquals(createAd.description, responseObj.ad?.description)
        assertEquals(createAd.adType, responseObj.ad?.adType)
        assertEquals(createAd.visibility, responseObj.ad?.visibility)
    }

    @Test
    fun read() = testApplication {
        val adCreateResponse = initObject(client)
        val oldId = adCreateResponse.ad?.id
        val response = client.post("/v2/ad/read") {

            val requestObj = AdReadRequest(
                requestId = "12345",
                ad = AdReadObject(oldId),
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV2Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV2Mapper.decodeFromString<AdReadResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(oldId, responseObj.ad?.id)
    }

    @Test
    fun update() = testApplication {
        val initObject = initObject(client)
        val adUpdate = AdUpdateObject(
            id = initObject.ad?.id,
            title = "Болт",
            description = "КРУТЕЙШИЙ",
            adType = DealSide.DEMAND,
            visibility = AdVisibility.PUBLIC,
        )

        val response = client.post("/v2/ad/update") {
            val requestObj = AdUpdateRequest(
                requestId = "12345",
                ad = adUpdate,
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV2Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV2Mapper.decodeFromString<AdUpdateResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(adUpdate.id, responseObj.ad?.id)
        assertEquals(adUpdate.title, responseObj.ad?.title)
        assertEquals(adUpdate.description, responseObj.ad?.description)
        assertEquals(adUpdate.adType, responseObj.ad?.adType)
        assertEquals(adUpdate.visibility, responseObj.ad?.visibility)
    }

    @Test
    fun delete() = testApplication {
        val initObject = initObject(client)
        val id = initObject.ad?.id
        val response = client.post("/v2/ad/delete") {
            val requestObj = AdDeleteRequest(
                requestId = "12345",
                ad = AdDeleteObject(
                    id = id,
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV2Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV2Mapper.decodeFromString<AdDeleteResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertEquals(id, responseObj.ad?.id)
    }

    @Test
    fun search() = testApplication {
        val initObject = initObject(client)
        val response = client.post("/v2/ad/search") {
            val requestObj = AdSearchRequest(
                requestId = "12345",
                adFilter = AdSearchFilter(),
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV2Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV2Mapper.decodeFromString<AdSearchResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.ads?.size)
        assertEquals(initObject.ad?.id, responseObj.ads?.first()?.id)
    }
    private suspend fun initObject(client: HttpClient): AdCreateResponse {
        val response = client.post("/v2/ad/create") {
            contentType(ContentType.Application.Json)
            val requestJson = apiV2Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        assertEquals(200, response.status.value)
        return apiV2Mapper.decodeFromString<AdCreateResponse>(responseJson)
    }

    @Test
    fun offers() = testApplication {
        val response = client.post("/v2/ad/offers") {
            val requestObj = AdOffersRequest(
                requestId = COMMON_REQUEST_ID,
                ad = AdReadObject(
                    id = uuidOld,
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            val requestJson = apiV2Mapper.encodeToString(requestObj)
            setBody(requestJson)
        }
        val responseJson = response.bodyAsText()
        val responseObj = apiV2Mapper.decodeFromString<AdOffersResponse>(responseJson)
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.ads?.size)
        assertEquals(COMMON_REQUEST_ID, responseObj.requestId)
    }
}
