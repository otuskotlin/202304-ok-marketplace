rootProject.name = "ok-marketplace-202304"

pluginManagement {
    val kotlinVersion: String by settings
    val openapiVersion: String by settings
    val springframeworkBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val pluginSpringVersion: String by settings
    val pluginJpa: String by settings
    val pluginShadow: String by settings
    val ktorVersion: String by settings
    val bmuschkoVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion apply false

        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.springframework.boot") version springframeworkBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("plugin.spring") version pluginSpringVersion apply false
        kotlin("plugin.jpa") version pluginJpa apply false

        id("io.ktor.plugin") version ktorVersion apply false
        id("com.bmuschko.docker-java-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-spring-boot-application") version bmuschkoVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
        id("com.github.johnrengelman.shadow") version pluginShadow apply false
    }
}

//include("m1l1-hello")
//include("m1l2-basic")
//include("m1l3-oop")
//include("m1l4-dsl")
//include("m1l5-coroutines")
//include("m1l6-flows-and-channels")
//include("m1l7-kmp")
//include("m3l1-spring")

include("ok-marketplace-acceptance")

include("ok-marketplace-api-v1-jackson")
include("ok-marketplace-api-v2-kmp")
include("ok-marketplace-api-log1")

include("ok-marketplace-common")
include("ok-marketplace-mappers-v1")
include("ok-marketplace-mappers-v2")
include("ok-marketplace-mappers-log1")

include("ok-marketplace-biz")
include("ok-marketplace-stubs")

include("ok-marketplace-app-common")
include("ok-marketplace-app-spring")
include("ok-marketplace-app-ktor")
include("ok-marketplace-app-serverless")
include("ok-marketplace-app-rabbit")
include("ok-marketplace-app-kafka")

include("ok-marketplace-lib-logging-common")
include("ok-marketplace-lib-logging-kermit")
include("ok-marketplace-lib-logging-logback")
