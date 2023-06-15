package net.longbowxxx.generativeai

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class GenerativeAiClientImplTest {

    // API Key がないと動かないので、普段は無効
    @Disabled
    @Test
    fun requestDiscuss() = runBlocking {
        val client = GenerativeAiClient(generativeAiSettings)
        client.requestDiscuss()
    }
}
