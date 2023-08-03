package ru.otus.otuskotlin.marketplace.blackbox.test

import io.kotest.core.annotation.Ignored
import ru.otus.otuskotlin.marketplace.blackbox.docker.RabbitDockerCompose
import ru.otus.otuskotlin.marketplace.blackbox.fixture.BaseFunSpec
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.RabbitClient

class AccRabbitTest : BaseFunSpec(RabbitDockerCompose, {
    val client = RabbitClient(RabbitDockerCompose)

    testApiV1(client)
    testApiV2(client)
})
