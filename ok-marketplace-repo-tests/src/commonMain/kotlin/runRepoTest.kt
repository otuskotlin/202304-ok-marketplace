package ru.otus.otuskotlin.marketplace.backend.repo.tests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
fun runRepoTest(testBody: suspend TestScope.() -> Unit) = runTest(dispatchTimeoutMs = 5*60*1000) {
    withContext(Dispatchers.Default) {
        testBody()
    }
}
