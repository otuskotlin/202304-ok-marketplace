package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as bs
import ru.otus.otuskotlin.marketplace.backend.repo.tests.runRepoTest
import kotlin.test.Test

class AdRepoGremlinCreateTest /*: RepoAdCreateTest()*/ {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun xx() = runRepoTest {
        val host = ArcadeDbContainer.container.host
        val port = ArcadeDbContainer.container.getMappedPort(8182)
        val cluster = Cluster.build().apply {
            addContactPoints(host)
            port(port)
            credentials(ArcadeDbContainer.username, ArcadeDbContainer.password)
//            enableSsl(enableSsl)
        }.create()
        traversal().withRemote(DriverRemoteConnection.using(cluster)).use { g ->
            g.addV("Test")
                .property("lock", "aaa")
                .next()
            val y = g.V().hasLabel("Test").`as`("a")
                .project<Any?>("lock", "ownerId", "z")
                .by("lock")
                .by(bs.inE("Owns").outV().id())
                .by(bs.elementMap<Vertex, Map<Any?, Any?>>())
                .toList()

            println("CONTENT: ${y}")
        }
    }

//    override val repo: IAdRepository by lazy {
//        AdRepoGremlin(
//            hosts = ArcadeDbContainer.container.host,
//            port = ArcadeDbContainer.container.getMappedPort(8182),
//            enableSsl = false,
//            user = ArcadeDbContainer.username,
//            pass = ArcadeDbContainer.password,
//            initObjects = RepoAdSearchTest.initObjects,
//            initRepo = { g -> g.V().drop().iterate() },
//            randomUuid = { lockNew.asString() }
//        )
//    }
}
