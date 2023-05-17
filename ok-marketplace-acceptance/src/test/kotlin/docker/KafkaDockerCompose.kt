package ru.otus.otuskotlin.marketplace.blackbox.docker

object KafkaDockerCompose : AbstractDockerCompose(
    "kafka_1", 9091, "kafka/docker-compose-kafka.yml"
)