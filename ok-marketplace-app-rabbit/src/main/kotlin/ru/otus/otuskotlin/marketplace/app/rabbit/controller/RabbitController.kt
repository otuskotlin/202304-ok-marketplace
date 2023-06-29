package ru.otus.otuskotlin.marketplace.app.rabbit.controller

import kotlinx.coroutines.*
import ru.otus.otuskotlin.marketplace.app.rabbit.RabbitProcessorBase
import ru.otus.otuskotlin.marketplace.app.rabbit.config.rabbitLogger

// TODO-rmq-5: запуск процессора
class RabbitController(
    private val processors: Set<RabbitProcessorBase>
) {

    fun start() = runBlocking {
        rabbitLogger.info("start init processors")
        processors.forEach {
            try {
                    launch { it.process() }
            } catch (e: RuntimeException) {
                // логируем, что-то делаем
                e.printStackTrace()
            }

        }
    }

}
