package ru.otus.otuskotlin.marketplace.blackbox.test

import ru.otus.otuskotlin.marketplace.blackbox.fixture.BaseFunSpec
import ru.otus.otuskotlin.marketplace.blackbox.docker.KafkaDockerCompose
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.KafkaClient

class AccKafkaTest : BaseFunSpec(KafkaDockerCompose, {
    val client = KafkaClient(KafkaDockerCompose)

    testStubApiV1(client)
    testStubApiV2(client)
})
