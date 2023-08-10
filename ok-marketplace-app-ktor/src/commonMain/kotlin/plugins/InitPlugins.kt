package ru.otus.otuskotlin.marketplace.app.plugins
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.websocket.*
import io.ktor.util.*
import ru.otus.otuskotlin.marketplace.app.common.MkplAppSettings

fun Application.initPlugins(appSettings: MkplAppSettings) {
    pluginRegistry.getOrNull(AttributeKey("WebSockets"))?: install(WebSockets)
    pluginRegistry.getOrNull(AttributeKey("CORS"))?: install(CORS) {
        allowNonSimpleContentTypes = true
        allowSameOrigin = true
        allowCredentials = true
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowHeader("*")
        appSettings.appUrls.forEach {
            val split = it.split("://")
            println("$split")
            when (split.size) {
                2 -> allowHost(
                    split[1].split("/")[0]/*.apply { log(module = "app", msg = "COR: $this") }*/,
                    listOf(split[0])
                )

                1 -> allowHost(
                    split[0].split("/")[0]/*.apply { log(module = "app", msg = "COR: $this") }*/,
                    listOf("http", "https")
                )
            }
        }
    }
     pluginRegistry.getOrNull(AttributeKey("Caching Headers"))?:install(CachingHeaders)
     pluginRegistry.getOrNull(AttributeKey("AutoHeadResponse"))?:install(AutoHeadResponse)
}
