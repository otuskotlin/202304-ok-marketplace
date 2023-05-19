package ru.otus.otuskotlin.marketplace.blackbox.test.action.v2

import mu.KotlinLogging
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestSerialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.api.v2.models.IResponse
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.Client

private val log = KotlinLogging.logger {}

suspend fun Client.sendAndReceive(path: String, request: IRequest): IResponse {
    val requestBody = apiV2RequestSerialize(request)
    log.warn { "Send to v2/$path\n$requestBody" }

    val responseBody = sendAndReceive("v2", path, requestBody)
    log.warn { "Received\n$responseBody" }

    return apiV2ResponseDeserialize(responseBody)
}