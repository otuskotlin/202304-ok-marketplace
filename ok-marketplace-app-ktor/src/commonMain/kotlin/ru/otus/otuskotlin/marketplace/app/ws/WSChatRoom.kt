package ru.otus.otuskotlin.marketplace.app.ws

import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private val sessions = mutableMapOf<WebSocketSession, UserSessionData>()

// Mutex protecting the cache mutable state
private val sessionsMutex = Mutex()

suspend fun WebSocketSession.wsChat() {
    send("Please enter your name (name: <name>) and message (msg: <message>) or close")

    incoming.receiveAsFlow().mapNotNull { frame ->
        if (frame !is Frame.Text) return@mapNotNull

        val text: String = frame.readText()

        try {
            when {
                text.startsWith("name:") -> {
                    val name = text.drop(5).trim()

                    sessionsMutex.withLock {
                        sessions.remove(this)
                        sessions[this] = UserSessionData(this, name)
                        println("Sessions: ${sessions.size}")
                    }

                    send("Hi, $name!")
                }

                text.startsWith("msg:") -> {
                    sessionsMutex.withLock {
                        val current = sessions.getValue(this)

                        sessions.filterKeys { it != this }.values.forEach {
                            val message = text.drop(4).trim()
                            println("Send to: ${it.name}")
                            it.session.send("[${current.name}]: $message")
                        }
                    }
                }

                text == "close" -> {
                    sessionsMutex.withLock {
                        val current = sessions.getValue(this)

                        close(CloseReason(CloseReason.Codes.NORMAL, "Client left chat room"))
                        sessions.remove(this)
                        sessions.values.forEach {
                            println("Send to: ${it.name}")
                            it.session.send("[${current.name}]: left.")
                        }
                    }
                }
            }
        } catch (_: ClosedReceiveChannelException) {
            sessionsMutex.withLock {
                sessions.remove(this)
            }
        } catch (e: Exception) {
            println("Error!")
            e.printStackTrace()
        }
    }.collect()
}

data class UserSessionData(
    val session: WebSocketSession,
    val name: String,
)
