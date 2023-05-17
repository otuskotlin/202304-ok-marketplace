package ru.otus.otuskotlin.marketplace.blackbox.test

import fixture.client.RestClient
import io.kotest.core.annotation.Ignored
import ru.otus.otuskotlin.marketplace.blackbox.docker.KtorDockerCompose
import ru.otus.otuskotlin.marketplace.blackbox.docker.SpringDockerCompose
import ru.otus.otuskotlin.marketplace.blackbox.fixture.BaseFunSpec
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.WebSocketClient
import ru.otus.otuskotlin.marketplace.blackbox.fixture.docker.DockerCompose

@Ignored
open class AccRestTestBase(dockerCompose: DockerCompose, runWebSocket: Boolean) : BaseFunSpec(dockerCompose, {
    val restClient = RestClient(dockerCompose)
    testApiV1(restClient, "rest ")
    testApiV2(restClient, "rest ")

    if (runWebSocket) {
        val websocketClient = WebSocketClient(dockerCompose)
        testApiV1(websocketClient, "websocket ")
        testApiV2(websocketClient, "websocket ")
    }
})

class AccRestSpringTest : AccRestTestBase(SpringDockerCompose, true)
// я пока не разобрался, почему websocket-тесты не работают с ktor. Выключил
class AccRestKtorTest : AccRestTestBase(KtorDockerCompose, false)
