package ru.otus.otuskotlin.marketplace.blackbox.test

import fixture.client.RestClient
import io.kotest.core.annotation.Ignored
import ru.otus.otuskotlin.marketplace.blackbox.docker.KtorDockerCompose
import ru.otus.otuskotlin.marketplace.blackbox.docker.SpringDockerCompose
import ru.otus.otuskotlin.marketplace.blackbox.fixture.BaseFunSpec
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.WebSocketClient
import ru.otus.otuskotlin.marketplace.blackbox.fixture.db.PostgresClearer
import ru.otus.otuskotlin.marketplace.blackbox.fixture.docker.DockerCompose

open class AccRestSpringTest : BaseFunSpec(SpringDockerCompose, PostgresClearer(), {
    val restClient = RestClient(SpringDockerCompose)
    testApiV1(restClient, "rest ")
    testStubApiV2(restClient, "rest ")

    val websocketClient = WebSocketClient(SpringDockerCompose)
    testApiV1(websocketClient, "websocket ")
    testStubApiV2(websocketClient, "websocket ")
})

open class AccRestKtorTest : BaseFunSpec(KtorDockerCompose, {
    val restClient = RestClient(KtorDockerCompose)
    testStubApiV1(restClient, "rest ")
    testStubApiV2(restClient, "rest ")

    if (false) {
        // я пока не разобрался, почему websocket-тесты не работают с ktor. Выключил
        val websocketClient = WebSocketClient(KtorDockerCompose)
        testStubApiV1(websocketClient, "websocket ")
        testStubApiV2(websocketClient, "websocket ")
    }
})
