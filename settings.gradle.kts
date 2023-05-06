rootProject.name = "ok-marketplace"
include("m1l1-hello")
include("m1l2-basic")
include("m1l3-oop")

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }
}
