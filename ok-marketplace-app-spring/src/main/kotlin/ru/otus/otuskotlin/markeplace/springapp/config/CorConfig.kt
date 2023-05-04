package ru.otus.otuskotlin.markeplace.springapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.marketplace.backend.repo.sql.RepoAdSQL
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoStub
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.logging.common.MpLoggerProvider
import ru.otus.otuskotlin.marketplace.logging.jvm.mpLoggerLogback

@Configuration
class CorConfig {
    @Bean
    fun loggerProvider(): MpLoggerProvider = MpLoggerProvider { mpLoggerLogback(it) }

    @Bean
    fun prodRepository() = RepoAdSQL()

    @Bean
    fun testRepository() = AdRepoInMemory()

    @Bean
    fun corSettings(
        @Qualifier("prodRepository") prodRepository: IAdRepository,
        @Qualifier("testRepository") testRepository: IAdRepository,
        @Qualifier("stubRepository") stubRepository: IAdRepository,
    ): MkplCorSettings = MkplCorSettings(
        loggerProvider = loggerProvider(),
        repoStub = stubRepository,
        repoTest = testRepository,
        repoProd = prodRepository,
    )

    @Bean
    fun processor(corSettings: MkplCorSettings) = MkplAdProcessor(corSettings)
}
