package ru.otus.otuskotlin.marketplace.blackbox.test.action.v1

import ru.otus.otuskotlin.marketplace.api.v1.models.*

val debug = AdDebug(mode = AdRequestDebugMode.STUB, stub = AdRequestDebugStubs.SUCCESS)
val prod = AdDebug(mode = AdRequestDebugMode.PROD)

val someCreateAd = AdCreateObject(
    title = "Требуется болт",
    description = "Требуется болт 100x5 с шестигранной шляпкой",
    adType = DealSide.DEMAND,
    visibility = AdVisibility.PUBLIC
)
