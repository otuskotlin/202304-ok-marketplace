package ru.otus.otuskotlin.markeplace.springapp.api.v1.ws

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import ru.otus.otuskotlin.marketplace.api.logs.mapper.toLog
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.process
import ru.otus.otuskotlin.marketplace.common.helpers.isUpdatableCommand
import ru.otus.otuskotlin.marketplace.mappers.v1.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v1.toTransportInit
import java.util.concurrent.ConcurrentHashMap

@Component
class WsAdHandlerV1(
    private val settings: MkplAppSettings
) : TextWebSocketHandler() {
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()
    private val logger = settings.logger.logger(WsAdHandlerV1::class)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions[session.id] = session

        runBlocking {
            settings.processor.process(logger, "ws-init",
                fromTransport = {},
                sendResponse = {
                    val msg = apiV1Mapper.writeValueAsString(toTransportInit())
                    session.sendMessage(TextMessage(msg))
                })
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        runBlocking {
            settings.processor.process(logger, "ws-v1",
                {
                    val request = apiV1Mapper.readValue(message.payload, IRequest::class.java)
                    fromTransport(request)
                },
                {
                    val result = apiV1Mapper.writeValueAsString(toTransportAd())
                    if (isUpdatableCommand()) {
                        sessions.values.forEach {
                            it.sendMessage(TextMessage(result))
                        }
                    } else {
                        session.sendMessage(TextMessage(result))
                    }
                })
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session.id)
    }
}
