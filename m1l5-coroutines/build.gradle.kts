plugins {
    kotlin("jvm")
}

val coroutinesVersion: String by project
val jacksonVersion: String by project
val okhttpVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion") // from string to object

    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion") // http client

    testImplementation(kotlin("test-junit"))
}
