package ru.otus.otuskotlin.marketplace.app.rabbit

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.testcontainers.containers.RabbitMQContainer
import ru.otus.otuskotlin.marketplace.api.v1.apiV1Mapper
import ru.otus.otuskotlin.marketplace.api.v1.models.AdCreateObject
import ru.otus.otuskotlin.marketplace.api.v1.models.AdCreateRequest
import ru.otus.otuskotlin.marketplace.api.v1.models.AdCreateResponse
import ru.otus.otuskotlin.marketplace.api.v1.models.AdDebug
import ru.otus.otuskotlin.marketplace.api.v1.models.AdRequestDebugMode
import ru.otus.otuskotlin.marketplace.api.v1.models.AdRequestDebugStubs
import ru.otus.otuskotlin.marketplace.api.v2.apiV2RequestSerialize
import ru.otus.otuskotlin.marketplace.api.v2.apiV2ResponseDeserialize
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitConfig.Companion.RABBIT_PASSWORD
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitConfig.Companion.RABBIT_USER
import ru.otus.otuskotlin.marketplace.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.otuskotlin.marketplace.app.rabbit.controller.RabbitController
import ru.otus.otuskotlin.marketplace.app.rabbit.processor.RabbitDirectProcessorV1
import ru.otus.otuskotlin.marketplace.app.rabbit.processor.RabbitDirectProcessorV2
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import ru.otus.otuskotlin.marketplace.api.v2.models.AdCreateObject as AdCreateObjectV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdCreateRequest as AdCreateRequestV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdCreateResponse as AdCreateResponseV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdDebug as AdDebugV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdRequestDebugMode as AdRequestDebugModeV2
import ru.otus.otuskotlin.marketplace.api.v2.models.AdRequestDebugStubs as AdRequestDebugStubsV2

//  TODO-rmq-8: тесты в использованием testcontainers
class RabbitMqTest {

    companion object {
        const val EXCHANGE_TYPE = "direct"
        const val TRANSPORT_EXCHANGE_V1 = "transport-exchange-v1"
        const val TRANSPORT_EXCHANGE_V2 = "transport-exchange-v2"
    }

    val container =
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
//            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
        RabbitMQContainer("rabbitmq:latest").apply {
            withExposedPorts(5672, 15672)
            withUser(RABBIT_USER, RABBIT_PASSWORD)
        }
    val config by lazy {
        RabbitConfig(
            port = container.getMappedPort(5672),
            host = container.host
        )
    }
    val processorConfigV1 = RabbitExchangeConfiguration(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = TRANSPORT_EXCHANGE_V1,
        queueIn = "v1-queue",
        queueOut = "v1-queue-out",
        consumerTag = "v1-consumer",
        exchangeType = EXCHANGE_TYPE
    )
    val processorV1 by lazy {
        RabbitDirectProcessorV1(
            config = config,
            processorConfig = processorConfigV1
        )
    }
    val processorConfigV2 = RabbitExchangeConfiguration(
        keyIn = "in-v2",
        keyOut = "out-v2",
        exchange = TRANSPORT_EXCHANGE_V2,
        queueIn = "v2-queue",
        queueOut = "v2-queue-out",
        consumerTag = "v2-consumer",
        exchangeType = EXCHANGE_TYPE
    )

    val processorV2 by lazy {


        RabbitDirectProcessorV2(
            config = config,
            processorConfig = processorConfigV2
        )
    }
    val controller by lazy {
        RabbitController(
            processors = setOf(processorV1, processorV2)
        )
    }

    @BeforeTest
    fun tearUp() {
        container.start()
        println("container started")
        GlobalScope.launch(Dispatchers.IO) {
            controller.start()
        }
        Thread.sleep(6000)
        // await when controller starts producers
        println("controller initiated")
    }

    @Test
    fun adCreateTestV1() {
        println("start test v1")
        val processorConfig = processorV1.processorConfig
        val keyIn = processorConfig.keyIn

        val connection1 = ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = config.user
            password = config.password
        }.newConnection()
        connection1.createChannel().use { channel ->
            var responseJson = ""
            channel.exchangeDeclare(processorConfig.exchange, EXCHANGE_TYPE)
            val queueOut = channel.queueDeclare().queue
            channel.queueBind(queueOut, processorConfig.exchange, processorConfig.keyOut)
            val deliverCallback = DeliverCallback { consumerTag, delivery ->
                responseJson = String(delivery.body, Charsets.UTF_8)
                println(" [x] Received by $consumerTag: '$responseJson'")
            }
            channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

            channel.basicPublish(processorConfig.exchange, keyIn, null, apiV1Mapper.writeValueAsBytes(boltCreateV1))

            Thread.sleep(3000)
            // waiting for message processing
            println("RESPONSE: $responseJson")
            val response = apiV1Mapper.readValue(responseJson, AdCreateResponse::class.java)
            val expected = MkplAdStub.get()

            assertEquals(expected.title, response.ad?.title)
            assertEquals(expected.description, response.ad?.description)
        }
    }

    @Test
    fun adCreateTestV2() {
        println("start test v2")
        val processorConfig = processorV2.processorConfig
        val keyIn = processorConfig.keyIn
        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = config.user
            password = config.password
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(processorConfig.exchange, EXCHANGE_TYPE)
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, processorConfig.exchange, processorConfig.keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(
                    processorConfig.exchange,
                    keyIn,
                    null,
                    apiV2RequestSerialize(boltCreateV2).toByteArray()
                )
                Thread.sleep(3000)
                // waiting for message processing

                println("RESPONSE: $responseJson")
                val response = apiV2ResponseDeserialize<AdCreateResponseV2>(responseJson)
                val expected = MkplAdStub.get()
                assertEquals(expected.title, response.ad?.title)
                assertEquals(expected.description, response.ad?.description)
            }
        }
    }

    private val boltCreateV1 = with(MkplAdStub.get()) {
        AdCreateRequest(
            ad = AdCreateObject(
                title = title,
                description = description
            ),
            requestType = "create",
            debug = AdDebug(
                mode = AdRequestDebugMode.STUB,
                stub = AdRequestDebugStubs.SUCCESS
            )
        )
    }

    private val boltCreateV2 = with(MkplAdStub.get()) {
        AdCreateRequestV2(
            ad = AdCreateObjectV2(
                title = title,
                description = description
            ),
            requestType = "create",
            debug = AdDebugV2(
                mode = AdRequestDebugModeV2.STUB,
                stub = AdRequestDebugStubsV2.SUCCESS
            )
        )
    }
}
