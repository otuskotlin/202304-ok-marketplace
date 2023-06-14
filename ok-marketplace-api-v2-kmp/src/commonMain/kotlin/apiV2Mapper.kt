package ru.otus.otuskotlin.marketplace.api.v2

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import ru.otus.otuskotlin.marketplace.api.v2.models.*

/**
 * Добавляйте сюда элементы при появлении новых наследников IRequest / IResponse
 */
internal val infos = listOf(
    info(AdCreateRequest::class, IRequest::class, "create") { copy(requestType = it) },
    info(AdReadRequest::class, IRequest::class, "read") { copy(requestType = it) },
    info(AdUpdateRequest::class, IRequest::class, "update") { copy(requestType = it) },
    info(AdDeleteRequest::class, IRequest::class, "delete") { copy(requestType = it) },
    info(AdSearchRequest::class, IRequest::class, "search") { copy(requestType = it) },
    info(AdOffersRequest::class, IRequest::class, "offers") { copy(requestType = it) },

    info(AdCreateResponse::class, IResponse::class, "create") { copy(responseType = it) },
    info(AdReadResponse::class, IResponse::class, "read") { copy(responseType = it) },
    info(AdUpdateResponse::class, IResponse::class, "update") { copy(responseType = it) },
    info(AdDeleteResponse::class, IResponse::class, "delete") { copy(responseType = it) },
    info(AdSearchResponse::class, IResponse::class, "search") { copy(responseType = it) },
    info(AdOffersResponse::class, IResponse::class, "offers") { copy(responseType = it) },
    info(AdInitResponse::class, IResponse::class, "init") { copy(responseType = it) },
)

val apiV2Mapper = Json {
    classDiscriminator = "_"
    encodeDefaults = true
    ignoreUnknownKeys = true

    serializersModule = SerializersModule {
        setupPolymorphic()
    }
}

fun apiV2RequestSerialize(request: IRequest): String = apiV2Mapper.encodeToString(request)

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV2RequestDeserialize(json: String): T =
    apiV2Mapper.decodeFromString<IRequest>(json) as T

fun apiV2ResponseSerialize(response: IResponse): String = apiV2Mapper.encodeToString(response)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiV2ResponseDeserialize(json: String): T =
    apiV2Mapper.decodeFromString<IResponse>(json) as T
