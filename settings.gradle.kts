rootProject.name = "ok-marketplace-202304"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false

        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.openapi.generator") version openapiVersion apply false
    }
}

//include("m1l1-hello")
//include("m1l2-basic")
//include("m1l3-oop")
//include("m1l4-dsl")
//include("m1l5-coroutines")
//include("m1l6-flows-and-channels")
//include("m1l7-kmp")

include("ok-marketplace-acceptance")

include("ok-marketplace-api-v1-jackson")
include("ok-marketplace-api-v2-kmp")
