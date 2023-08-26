package ru.otus.otuskotlin.marketplace.blackbox.test

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldExistInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.marketplace.api.v1.models.AdSearchFilter
import ru.otus.otuskotlin.marketplace.api.v1.models.AdUpdateObject
import ru.otus.otuskotlin.marketplace.api.v1.models.DealSide
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.Client
import ru.otus.otuskotlin.marketplace.blackbox.test.action.v1.*

fun FunSpec.testApiV1(client: Client, prefix: String = "") {
    context("${prefix}v1 prod") {
        test("Create Ad ok") {
            client.createAd(mode = prod)
        }

        test("Read Ad ok") {
            val created = client.createAd(mode = prod)
            client.readAd(created.id, mode = prod).asClue {
                it shouldBe created
            }
        }

        test("Update Ad ok") {
            val created = client.createAd(mode = prod)
            client.updateAd(
                created.id,
                created.lock,
                AdUpdateObject(title = "Selling Nut", description = someCreateAd.description),
                mode = prod
            )
            client.readAd(created.id, mode = prod) {
                it.ad?.title shouldBe "Selling Nut"
                it.ad?.description shouldBe someCreateAd.description
            }
        }

        test("Delete Ad ok") {
            val created = client.createAd(mode = prod)
            client.deleteAd(created.id, created.lock, mode = prod)
            client.readAd(created.id, mode = prod) {
                it should haveError("not-found")
            }
        }

        test("Search Ad ok") {
            client.createAd(someCreateAd.copy(title = "Selling Bolt"), mode = prod)
            client.createAd(someCreateAd.copy(title = "Selling Nut"), mode = prod)

            withClue("Search Selling") {
                val results = client.searchAd(search = AdSearchFilter(searchString = "Selling"), mode = prod)
                results shouldHaveSize 2
                results shouldExist { it.title == "Selling Bolt" }
                results shouldExist { it.title == "Selling Nut" }
            }

            withClue("Search Bolt") {
                val results = client.searchAd(search = AdSearchFilter(searchString = "Bolt"), mode = prod)
                results.shouldExistInOrder({ it.title == "Selling Bolt" })
            }

            withClue("Search NotExisted") {
                val results = client.searchAd(search = AdSearchFilter(searchString = "NotExisted"), mode = prod)
                results shouldHaveSize 0
            }
        }

        test("Offer Ad ok") {
            val supply = client.createAd(someCreateAd.copy(title = "Some Bolt", adType = DealSide.SUPPLY), mode = prod)
            val demand = client.createAd(someCreateAd.copy(title = "Some Bolt", adType = DealSide.DEMAND), mode = prod)

            withClue("Find offer for supply") {
                client.offersAd(supply.id, mode = prod)
                .shouldExistInOrder({it.id == demand.id })
            }

            withClue("Find offer for demand") {
                client.offersAd(demand.id, mode = prod)
                .shouldExistInOrder({it.id == supply.id })
            }
        }
    }

}