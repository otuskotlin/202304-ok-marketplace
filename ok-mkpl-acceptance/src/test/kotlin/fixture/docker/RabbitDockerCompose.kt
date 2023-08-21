package ru.otus.otuskotlin.marketplace.blackbox.docker

object RabbitDockerCompose : AbstractDockerCompose(
    "rabbit_1", 5672, "rabbit/docker-compose-rabbit.yml"
) {
    override val user: String
        get() = "rabbitmq"
    override val password: String
        get() = "rabbitmq"
}
