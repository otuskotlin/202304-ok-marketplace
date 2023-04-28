package ru.otus.otuskotlin.marketplace.blackbox.docker

object KtorDockerCompose : AbstractDockerCompose(
    "app-ktor_1", 8080, "ktor/docker-compose-ktor.yml"
)