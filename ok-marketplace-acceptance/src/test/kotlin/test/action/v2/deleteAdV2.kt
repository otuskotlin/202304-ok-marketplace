package ru.otus.otuskotlin.marketplace.blackbox.test.action.v2

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.blackbox.test.action.beValidId
import ru.otus.otuskotlin.marketplace.blackbox.test.action.beValidLock
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.Client

suspend fun Client.deleteAd(id: String?, lock: String?) {
    withClue("deleteAdV2: $id, lock: $lock") {
        id should beValidId
        lock should beValidLock

        val response = sendAndReceive(
            "ad/delete",
            AdDeleteRequest(
                requestType = "delete",
                debug = debug,
                ad = AdDeleteObject(id = id, lock = lock)
            )
        ) as AdDeleteResponse

        response.asClue {
            response should haveSuccessResult
            response.ad shouldNotBe null
            response.ad?.id shouldBe id
        }
    }
}