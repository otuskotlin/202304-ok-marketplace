package ru.otus.otuskotlin.markeplace.springapp.api.v2.ws

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import ru.otus.otuskotlin.markeplace.springapp.models.MkplAppSettings
import ru.otus.otuskotlin.marketplace.api.v2.apiV2Mapper
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.helpers.isUpdatableCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportInit
import java.util.concurrent.ConcurrentHashMap

@Component
class
WsAdHandlerV2(private val appSettings: MkplAppSettings) : TextWebSocketHandler() {
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) = runBlocking {
        sessions[session.id] = session

        val context = MkplContext()
        // TODO убрать, когда заработает обычный режим
        context.workMode = MkplWorkMode.STUB
        appSettings.processor.exec(context)

        val msg = apiV2ResponseSerialize(context.toTransportInit())
        session.sendMessage(TextMessage(msg))
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) = runBlocking {
        val ctx = MkplContext(timeStart = Clock.System.now())

        try {
            val request = apiV2Mapper.decodeFromString<IRequest>(message.payload)
            ctx.fromTransport(request)
            appSettings.processor.exec(ctx)

            val result = apiV2Mapper.encodeToString(ctx.toTransportAd())

            if (ctx.isUpdatableCommand()) {
                sessions.values.forEach {
                    it.sendMessage(TextMessage(result))
                }
            } else {
                session.sendMessage(TextMessage(result))
            }
        } catch (e: Exception) {
            ctx.errors.add(e.asMkplError())

            val response = apiV2ResponseSerialize(ctx.toTransportInit())
            session.sendMessage(TextMessage(response))
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session.id)
    }
}
