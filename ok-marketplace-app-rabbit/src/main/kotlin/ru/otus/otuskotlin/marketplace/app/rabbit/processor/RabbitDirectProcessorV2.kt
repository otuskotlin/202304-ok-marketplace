package ru.otus.otuskotlin.marketplace.app.rabbit.processor

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import ru.otus.otuskotlin.marketplace.api.logs.mapper.toLog
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.marketplace.api.v2.models.IRequest
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.process
import ru.otus.otuskotlin.marketplace.app.rabbit.RabbitProcessorBase
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.otuskotlin.marketplace.app.rabbit.config.corSettings
import ru.otus.otuskotlin.marketplace.app.rabbit.config.rabbitLogger
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportAd

class RabbitDirectProcessorV2(
    config: RabbitConfig,
    processorConfig: RabbitExchangeConfiguration,
    settings: MkplAppSettings = corSettings,
) : RabbitProcessorBase(config, processorConfig) {

    private val logger = settings.logger.logger(RabbitDirectProcessorV1::class)
    private val processor = settings.processor

    override suspend fun Channel.processMessage(message: Delivery) {
        processor.process(logger, "rabbit-v2",
            {
                apiV2RequestDeserialize<IRequest>(String(message.body)).also {
                    println("TYPE: ${it::class.java.simpleName}")
                    fromTransport(it)
                }
            },
            {
                rabbitLogger.info("start publish")
                val response = toTransportAd()
                apiV2ResponseSerialize(response).also {
                    println("Publishing $response to ${processorConfig.exchange} exchange for keyOut ${processorConfig.keyOut}")
                    basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it.toByteArray())
                }.also {
                    println("published")
                }
            },
            { toLog("rabbit-v2" ) })
    }
}

