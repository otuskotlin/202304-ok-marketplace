package ru.otus.otuskotlin.marketplace.blackbox.test

import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import ru.otus.otuskotlin.marketplace.api.v2.models.AdSearchFilter
import ru.otus.otuskotlin.marketplace.api.v2.models.AdUpdateObject
import ru.otus.otuskotlin.marketplace.api.v2.models.DealSide
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.Client
import ru.otus.otuskotlin.marketplace.blackbox.test.action.v2.*

fun FunSpec.testApiV2(client: Client, prefix: String = "") {
    context("${prefix}v2") {
        test("Create Ad ok") {
            client.createAd()
        }

        test("Read Ad ok") {
            val created = client.createAd()
            client.readAd(created.id).asClue {
                it shouldBe created
            }
        }

        test("Update Ad ok") {
            val created = client.createAd()
            client.updateAd(created.id, created.lock, AdUpdateObject(title = "Selling Nut"))
            client.readAd(created.id) {
                // TODO раскомментировать, когда будет реальный реп
                //it.ad?.title shouldBe "Selling Nut"
                //it.ad?.description shouldBe someCreateAd.description
            }
        }

        test("Delete Ad ok") {
            val created = client.createAd()
            client.deleteAd(created.id, created.lock)
            client.readAd(created.id) {
                // it should haveError("not-found") TODO раскомментировать, когда будет реальный реп
            }
        }

        test("Search Ad ok") {
            val created1 = client.createAd(someCreateAd.copy(title = "Selling Bolt"))
            val created2 = client.createAd(someCreateAd.copy(title = "Selling Nut"))

            withClue("Search Selling") {
                val results = client.searchAd(search = AdSearchFilter(searchString = "Selling"))
                // TODO раскомментировать, когда будет реальный реп
                // results shouldHaveSize 2
                // results shouldExist { it.title == "Selling Bolt" }
                // results shouldExist { it.title == "Selling Nut" }
            }

            withClue("Search Bolt") {
                client.searchAd(search = AdSearchFilter(searchString = "Bolt"))
                // TODO раскомментировать, когда будет реальный реп
                // .shouldExistInOrder({ it.title == "Selling Bolt" })
            }
        }

        test("Offer Ad ok") {
            val supply = client.createAd(someCreateAd.copy(title = "Some Bolt", adType = DealSide.SUPPLY))
            val demand = client.createAd(someCreateAd.copy(title = "Some Bolt", adType = DealSide.DEMAND))

            withClue("Find offer for supply") {
                client.offersAd(supply.id)
                // TODO раскомментировать, когда будет реальный реп
                // .shouldExistInOrder({it.id == demand.id })
            }

            withClue("Find offer for demand") {
                client.offersAd(demand.id)
                // TODO раскомментировать, когда будет реальный реп
                // .shouldExistInOrder({it.id == supply.id })
            }
        }
    }

}