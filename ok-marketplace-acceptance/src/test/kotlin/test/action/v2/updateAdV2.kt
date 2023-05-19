package ru.otus.otuskotlin.marketplace.blackbox.test.action.v2

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.blackbox.test.action.beValidId
import ru.otus.otuskotlin.marketplace.blackbox.test.action.beValidLock
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.Client

suspend fun Client.updateAd(id: String?, lock: String?, ad: AdUpdateObject): AdResponseObject =
    updateAd(id, lock, ad) {
        it should haveSuccessResult
        it.ad shouldBe adStub
        it.ad!!
    }

suspend fun <T> Client.updateAd(id: String?, lock: String?, ad: AdUpdateObject, block: (AdUpdateResponse) -> T): T =
    withClue("updatedV1: $id, lock: $lock, set: $ad") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "ad/update", AdUpdateRequest(
                requestType = "update",
                debug = debug,
                ad = ad.copy(id = id, lock = lock)
            )
        ) as AdUpdateResponse

        response.asClue(block)
    }
