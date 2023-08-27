package ru.otus.otuskotlin.marketplace.app.auth

import io.ktor.client.request.*
import ru.otus.otuskotlin.marketplace.app.common.AuthConfig

actual fun HttpRequestBuilder.addAuth(
    id: String,
    groups: List<String>,
    config: AuthConfig
) {
}
