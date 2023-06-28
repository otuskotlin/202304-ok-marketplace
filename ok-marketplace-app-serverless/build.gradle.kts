val jacksonVersion: String by project
val serializationVersion: String by project
val yandexCloudSdkVersion: String by project

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation("com.yandex.cloud:java-sdk-functions:$yandexCloudSdkVersion")
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    // transport models
    implementation(project(":ok-marketplace-common"))
    implementation(project(":ok-marketplace-api-v1-jackson"))
    implementation(project(":ok-marketplace-api-v2-kmp"))
    implementation(project(":ok-marketplace-mappers-v1"))
    implementation(project(":ok-marketplace-mappers-v2"))

    // Stubs
    implementation(project(":ok-marketplace-stubs"))
}