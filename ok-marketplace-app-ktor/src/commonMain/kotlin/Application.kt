package ru.otus.otuskotlin.marketplace.app

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.plugins.initAppSettings
import ru.otus.otuskotlin.marketplace.app.v2.WsHandlerV2
import ru.otus.otuskotlin.marketplace.app.v2.v2Ad
import ru.otus.otuskotlin.marketplace.app.v2.v2Offer

fun Application.module(appSettings: MkplAppSettings = initAppSettings(), installPlugins: Boolean = true) {
    if (installPlugins) {
        install(WebSockets)
    }

    val wsHandlerV2 = WsHandlerV2()

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("v2") {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }

            v2Ad(appSettings)
            v2Offer(appSettings)
        }

        webSocket("/ws/v2") {
            wsHandlerV2.handle(this, appSettings)
        }
    }
}

fun main() {
    embeddedServer(CIO, port = 8080, module = Application::module).start(wait = true)
}
