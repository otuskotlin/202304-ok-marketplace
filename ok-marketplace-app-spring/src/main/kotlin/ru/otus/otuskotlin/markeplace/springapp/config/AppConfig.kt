package ru.otus.otuskotlin.markeplace.springapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.marketplace.app.common.LoggerType
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.common.getLoggerProviderConf
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider

@Configuration
class AppConfig {
    @Bean
    fun loggerProvider(): MpLoggerProvider = getLoggerProviderConf(LoggerType.LOGBACK)

    @Bean
    fun processor() = MkplAdProcessor(corSettings())

    @Bean
    fun corSettings(): MkplCorSettings = MkplCorSettings(
        loggerProvider = loggerProvider(),
    )

    @Bean
    fun appSettings() = MkplAppSettings(
        processor = processor(),
        logger = loggerProvider(),
    )
}
