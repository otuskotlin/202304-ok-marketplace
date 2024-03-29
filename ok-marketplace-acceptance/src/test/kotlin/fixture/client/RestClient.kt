package fixture.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.Client
import ru.otus.otuskotlin.marketplace.blackbox.fixture.docker.DockerCompose

private const val TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJhZC11c2VycyIsImlzcyI6Ik90dXNLb3RsaW4iLCJncm91cHMiOlsiVVNFUiJdfQ.Ef_RcXDSuVU4P9bEDH5FwUrPioToz3H_Plylpuc2C1M"

/**
 * Отправка запросов по http/rest
 */
class RestClient(dockerCompose: DockerCompose) : Client {
    private val urlBuilder by lazy { dockerCompose.inputUrl }
    private val client = HttpClient(OkHttp)
    override suspend fun sendAndReceive(version: String, path: String, request: String): String {
        val url = urlBuilder.apply {
            path("$version/$path")
        }.build()

        val resp = client.post {
            url(url)
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
                append(HttpHeaders.Authorization, "Bearer $TOKEN")
            }
            accept(ContentType.Application.Json)
            setBody(request)

        }.call

        return resp.body()
    }
}