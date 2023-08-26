package ru.otus.otuskotlin.marketplace.blackbox.fixture

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase
import mu.KotlinLogging
import ru.otus.otuskotlin.marketplace.blackbox.fixture.db.DbClearer
import ru.otus.otuskotlin.marketplace.blackbox.fixture.docker.DockerCompose

private val log = KotlinLogging.logger {}

/**
 * Базовая реализация тестов, которая выполняет запуск и останов контейнеров, а также очистку БД.
 * Основана на FunSpec
 */
abstract class BaseFunSpec(
    private val dockerCompose: DockerCompose,
    private val dbClearer: DbClearer,
    body: FunSpec.() -> Unit) : FunSpec(body) {

    constructor(dockerCompose: DockerCompose, body: FunSpec.() -> Unit) : this(dockerCompose, DbClearerStub, body)

    override suspend fun afterSpec(spec: Spec) {
        clearDb()
        dockerCompose.stop()
        dbClearer.close()
    }

    override suspend fun beforeSpec(spec: Spec) {
        dockerCompose.start()
    }

    override suspend fun beforeEach(testCase: TestCase) {
        clearDb()
    }

    private fun clearDb() {
        log.warn("===== clearDb =====")
        dbClearer.clear()
        log.warn("===== clearDb complete =====")
    }
}

private object DbClearerStub : DbClearer {
    override fun clear() {
        log.info("     dbClearerStub")
    }

    override fun close() {
    }
}