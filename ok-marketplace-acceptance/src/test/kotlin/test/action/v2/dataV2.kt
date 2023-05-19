package ru.otus.otuskotlin.marketplace.blackbox.test.action.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.*


val debug = AdDebug(mode = AdRequestDebugMode.STUB, stub = AdRequestDebugStubs.SUCCESS)

val adStub = AdResponseObject(
    title = "Требуется болт",
    description = "Требуется болт 100x5 с шистигранной шляпкой",
    adType = DealSide.DEMAND,
    visibility = AdVisibility.PUBLIC,
    id = "666",
    ownerId = "user-1",
    permissions = setOf(
        AdPermissions.DELETE,
        AdPermissions.READ,
        AdPermissions.MAKE_VISIBLE_OWN,
        AdPermissions.UPDATE,
        AdPermissions.MAKE_VISIBLE_PUBLIC,
        AdPermissions.MAKE_VISIBLE_GROUP
    )
)

val someCreateAd = AdCreateObject(
    title = "Selling Bolt",
    description = "Some bolt",
    adType = DealSide.SUPPLY,
    visibility = AdVisibility.PUBLIC
)
