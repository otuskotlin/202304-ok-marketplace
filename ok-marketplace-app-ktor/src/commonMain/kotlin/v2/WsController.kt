package ru.otus.otuskotlin.marketplace.app.v2

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import ru.otus.otuskotlin.marketplace.api.logs.mapper.toLog
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.process
import ru.otus.otuskotlin.marketplace.common.helpers.isUpdatableCommand
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportInit

class WsHandlerV2 {
    private val mutex = Mutex()
    private val sessions = mutableSetOf<WebSocketSession>()

    suspend fun handle(session: WebSocketSession, appSettings: MkplAppSettings) {
        mutex.withLock {
            sessions.add(session)
        }

        val logger = appSettings.logger.logger(WsHandlerV2::class)
        val processor = appSettings.processor

        // Handle init request
        processor.process(logger, "init",
            fromTransport = {},
            sendResponse = {
                val init = apiV2ResponseSerialize(toTransportInit())
                session.outgoing.send(Frame.Text(init))
            },
            toLog = { toLog("init") }
        )


        // Handle flow
        session.incoming.receiveAsFlow().mapNotNull { it ->
            val frame = it as? Frame.Text ?: return@mapNotNull

            val jsonStr = frame.readText()

            // Handle without flow destruction
            processor.process(logger, "webSocket",
                fromTransport = {
                    val request = apiV2Mapper.decodeFromString<IRequest>(jsonStr)
                    fromTransport(request)
                },
                sendResponse = {
                    try {
                        val result = apiV2ResponseSerialize(toTransportAd())

                        // If change request, response is sent to everyone
                        if (isUpdatableCommand()) {
                            mutex.withLock {
                                sessions.forEach {
                                    it.send(Frame.Text(result))
                                }
                            }
                        } else {
                            session.outgoing.send(Frame.Text(result))
                        }
                    } catch (_: ClosedReceiveChannelException) {
                        mutex.withLock {
                            sessions.clear()
                        }
                    }
                },
                toLog = { toLog("webSocket") })
        }.collect()
    }
}
