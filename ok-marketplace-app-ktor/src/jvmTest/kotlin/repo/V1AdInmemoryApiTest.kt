package ru.otus.otuskotlin.marketplace.app.repo

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

const val COMMON_REQUEST_ID = "12345"
class V1AdInmemoryApiTest {
    private val createAd = AdCreateObject(
        title = "Болт",
        description = "КРУТЕЙШИЙ",
        adType = DealSide.DEMAND,
        visibility = AdVisibility.PUBLIC,
    )
    private val requestCreateObj = AdCreateRequest(
        requestId = "12345",
        ad = createAd,
        debug = AdDebug(
            mode = AdRequestDebugMode.TEST,
        )
    )

    @Test
    fun create() = testApplication {
        val client = myClient()
        val responseObj = initObject(client)
        assertEquals(createAd.title, responseObj.ad?.title)
        assertEquals(createAd.description, responseObj.ad?.description)
        assertEquals(createAd.adType, responseObj.ad?.adType)
        assertEquals(createAd.visibility, responseObj.ad?.visibility)
    }

    @Test
    fun read() = testApplication {
        val client = myClient()
        val id = initObject(client).ad?.id
        val response = client.post("/v1/ad/read") {
            val requestObj = AdReadRequest(
                requestId = "12345",
                ad = AdReadObject(id),
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(id, responseObj.ad?.id)
    }

    @Test
    fun update() = testApplication {
        val client = myClient()

        val created = initObject(client)

        val adUpdate = AdUpdateObject(
            id = created.ad?.id,
            title = "Болт",
            description = "КРУТЕЙШИЙ",
            adType = DealSide.DEMAND,
            visibility = AdVisibility.PUBLIC,
            lock = created.ad?.lock,
        )

        val response = client.post("/v1/ad/update") {
            val requestObj = AdUpdateRequest(
                requestId = "12345",
                ad = adUpdate,
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(adUpdate.id, responseObj.ad?.id)
        assertEquals(adUpdate.title, responseObj.ad?.title)
        assertEquals(adUpdate.description, responseObj.ad?.description)
        assertEquals(adUpdate.adType, responseObj.ad?.adType)
        assertEquals(adUpdate.visibility, responseObj.ad?.visibility)
    }

    @Test
    fun delete() = testApplication {
        val client = myClient()
        val created = initObject(client)

        val response = client.post("/v1/ad/delete") {
            val requestObj = AdDeleteRequest(
                requestId = "12345",
                ad = AdDeleteObject(
                    id = created.ad?.id,
                    lock = created.ad?.lock
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(created.ad?.id, responseObj.ad?.id)
    }

    @Test
    fun search() = testApplication {
        val client = myClient()
        val initObject = initObject(client)
        val response = client.post("/v1/ad/search") {
            val requestObj = AdSearchRequest(
                requestId = "12345",
                adFilter = AdSearchFilter(),
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.ads?.size)
        assertEquals(initObject.ad?.id, responseObj.ads?.first()?.id)
    }

    @Test
    fun offers() = testApplication {
        val client = myClient()
        val oldId = initObject(client).ad?.id
        val response = client.post("/v1/ad/offers") {
            val requestObj = AdOffersRequest(
                requestId = COMMON_REQUEST_ID,
                ad = AdReadObject(
                    id = oldId,
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdOffersResponse>()
        assertEquals(200, response.status.value)
        assertEquals(COMMON_REQUEST_ID, responseObj.requestId)
    }
    private suspend fun initObject(client: HttpClient): AdCreateResponse {
        val responseCreate = client.post("/v1/ad/create") {
            contentType(ContentType.Application.Json)
            setBody(requestCreateObj)
        }
        assertEquals(200, responseCreate.status.value)
        return responseCreate.body<AdCreateResponse>()
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }
}
