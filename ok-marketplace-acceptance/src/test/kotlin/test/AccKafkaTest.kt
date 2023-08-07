package ru.otus.otuskotlin.marketplace.blackbox.test

import io.kotest.core.annotation.Ignored
import ru.otus.otuskotlin.marketplace.blackbox.fixture.BaseFunSpec
import ru.otus.otuskotlin.marketplace.blackbox.docker.KafkaDockerCompose
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.KafkaClient

class AccKafkaTest : BaseFunSpec(KafkaDockerCompose, {
    val client = KafkaClient(KafkaDockerCompose)

    testApiV1(client)
    testApiV2(client)
})
