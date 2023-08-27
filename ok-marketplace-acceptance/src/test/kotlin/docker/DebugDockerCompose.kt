package ru.otus.otuskotlin.marketplace.blackbox.docker

import io.ktor.http.*
import ru.otus.otuskotlin.marketplace.blackbox.fixture.docker.DockerCompose

// для отладки тестов, предполагается, что докер-компоуз запущен вручную
object DebugDockerCompose : DockerCompose  {
    override fun start() {
    }

    override fun stop() {
    }

    override val inputUrl: URLBuilder
        get() = URLBuilder(
            protocol = URLProtocol.HTTP,
            host = "localhost",
            port = 8080,
        )
}