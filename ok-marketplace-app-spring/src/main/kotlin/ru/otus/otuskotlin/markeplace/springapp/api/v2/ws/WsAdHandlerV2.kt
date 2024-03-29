package ru.otus.otuskotlin.markeplace.springapp.api.v2.ws

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import ru.otus.otuskotlin.markeplace.springapp.fakeMkplPrincipal
import ru.otus.otuskotlin.marketplace.api.logs.mapper.toLog
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.process
import ru.otus.otuskotlin.marketplace.common.helpers.isUpdatableCommand
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportInit
import java.util.concurrent.ConcurrentHashMap

@Component
class WsAdHandlerV2(
    private val settings: MkplAppSettings
) : TextWebSocketHandler() {
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()
    private val logger = settings.logger.logger(WsAdHandlerV2::class)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions[session.id] = session

        runBlocking {
            settings.processor.process(logger, "ws-init",
                fromTransport = {
                    principal = fakeMkplPrincipal()
                },
                sendResponse = {
                    val msg = apiV2Mapper.encodeToString(toTransportInit())
                    session.sendMessage(TextMessage(msg))
                },
                toLog = { toLog("ws-init") })
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        runBlocking {
            settings.processor.process(logger, "ws-v1",
                {
                    val request = apiV2Mapper.decodeFromString<IRequest>(message.payload)
                    fromTransport(request)
                    principal = fakeMkplPrincipal()
                },
                {
                    val result = apiV2Mapper.encodeToString(toTransportAd())
                    if (isUpdatableCommand()) {
                        sessions.values.forEach {
                            it.sendMessage(TextMessage(result))
                        }
                    } else {
                        session.sendMessage(TextMessage(result))
                    }
                },
                {
                    toLog("ws-v1")
                })
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session.id)
    }
}
