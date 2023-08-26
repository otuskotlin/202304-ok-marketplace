package ru.otus.otuskotlin.marketplace.blackbox.test.action.v1

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.Client

suspend fun Client.createAd(ad: AdCreateObject = someCreateAd, mode: AdDebug = debug): AdResponseObject =
    createAd(ad, mode) {
        it should haveSuccessResult
        it.ad shouldNotBe null
        it.ad?.apply {
            title shouldBe ad.title
            description shouldBe ad.description
            adType shouldBe ad.adType
            visibility shouldBe ad.visibility
        }
        it.ad!!
    }

suspend fun <T> Client.createAd(
    ad: AdCreateObject = someCreateAd,
    mode: AdDebug = debug,
    block: (AdCreateResponse) -> T
): T =
    withClue("createAdV1: $ad") {
        val response = sendAndReceive(
            "ad/create", AdCreateRequest(
                requestType = "create",
                debug = mode,
                ad = ad
            )
        ) as AdCreateResponse

        response.asClue(block)
    }
