package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.otus.otuskotlin.marketplace.app.MkplAppSettings

fun Route.v2Ad(appSettings: MkplAppSettings) {
    val processor = appSettings.processor
    route("ad") {
        post("create") {
            call.createAd(appSettings)
        }
        post("read") {
            call.readAd(appSettings)
        }
        post("update") {
            call.updateAd(processor)
        }
        post("delete") {
            call.deleteAd(processor)
        }
        post("search") {
            call.searchAd(processor)
        }
    }
}

fun Route.v2Offer(appSettings: MkplAppSettings) {
    route("ad") {
        post("offers") {
            call.offersAd(appSettings)
        }
    }
}
