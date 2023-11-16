package net.longbowxxx.search

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class GoogleCustomSearchClientTest {
    @Disabled
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun search() =
        runTest {
            val client = SearchClient(googleCustomSearchClientSettings)
            val response =
                client.search(
                    SearchRequest(
                        "kotlin",
                    ),
                )

            println(response.results.toString())
        }
}
