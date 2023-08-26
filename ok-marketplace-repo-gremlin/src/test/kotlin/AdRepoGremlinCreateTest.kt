package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import ru.otus.otuskotlin.marketplace.backend.repo.tests.RepoAdCreateTest
import ru.otus.otuskotlin.marketplace.backend.repo.tests.RepoAdSearchTest
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import kotlin.test.Ignore

@Ignore("Выключен из-за проблем в github")
class AdRepoGremlinCreateTest : RepoAdCreateTest() {
    override val repo: IAdRepository by lazy {
        AdRepoGremlin(
            hosts = ArcadeDbContainer.container.host,
            port = ArcadeDbContainer.container.getMappedPort(8182),
            enableSsl = false,
            user = ArcadeDbContainer.username,
            pass = ArcadeDbContainer.password,
            initObjects = RepoAdSearchTest.initObjects,
            initRepo = { g -> g.V().drop().iterate() },
            randomUuid = { lockNew.asString() }
        )
    }
}
