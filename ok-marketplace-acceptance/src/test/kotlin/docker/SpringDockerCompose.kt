package ru.otus.otuskotlin.marketplace.blackbox.docker

object SpringDockerCompose : AbstractDockerCompose(
    "app-spring_1", 8080, "spring/docker-compose-spring.yml"
)