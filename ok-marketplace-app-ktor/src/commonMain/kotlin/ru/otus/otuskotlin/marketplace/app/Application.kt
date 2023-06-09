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
import ru.otus.otuskotlin.marketplace.app.v2.v2Ad
import ru.otus.otuskotlin.marketplace.app.v2.v2Offer
import ru.otus.otuskotlin.marketplace.app.v2.wsHandlerV2
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor

fun Application.module(processor: MkplAdProcessor = MkplAdProcessor(), installPlugins: Boolean = true) {
    if (installPlugins) {
        install(WebSockets)
    }

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("v2") {
            install(ContentNegotiation) {
                json(apiV2Mapper)
            }

            v2Ad(processor)
            v2Offer(processor)
        }

        webSocket("/ws/v2") {
            wsHandlerV2(processor)
        }
    }
}

fun main() {
    embeddedServer(CIO, port = 8080, module = Application::module).start(wait = true)
}
