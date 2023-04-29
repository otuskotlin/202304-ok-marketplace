rootProject.name = "ok-marketplace"
include("m1l1-hello")

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }
}
