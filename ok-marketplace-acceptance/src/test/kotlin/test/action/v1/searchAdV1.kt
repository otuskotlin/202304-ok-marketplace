package ru.otus.otuskotlin.marketplace.blackbox.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.Client

suspend fun Client.searchAd(search: AdSearchFilter, mode: AdDebug = debug): List<AdResponseObject> = searchAd(search, mode) {
    it should haveSuccessResult
    it.ads ?: listOf()
}

suspend fun <T> Client.searchAd(search: AdSearchFilter, mode: AdDebug = debug, block: (AdSearchResponse) -> T): T =
    withClue("searchAdV1: $search") {
        val response = sendAndReceive(
            "ad/search",
            AdSearchRequest(
                requestType = "search",
                debug = mode,
                adFilter = search,
            )
        ) as AdSearchResponse

        response.asClue(block)
    }
