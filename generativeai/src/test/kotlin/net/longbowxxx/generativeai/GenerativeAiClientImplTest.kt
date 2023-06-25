package net.longbowxxx.generativeai

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class GenerativeAiClientImplTest {

    // API Key がないと動かないので、普段は無効
    @Disabled
    @Test
    fun requestDiscuss(): Unit = runBlocking {
        val discussRequest = DiscussRequest(
            DISCUSS_MODEL,
            DiscussPrompt(
                listOf(DiscussMessage("How tall is the Eiffel Tower?", "0")),
                listOf(
                    DiscussExample(
                        DiscussMessage(content = "What is the capital of California?"),
                        DiscussMessage(content = "If the capital of California is what you seek, Sacramento is where you ought to peek."),
                    ),
                ),
                "Respond to all questions with a rhyming poem.",
            ),
            0.5f,
            1,
        )
        val client = GenerativeAiClient(generativeAiSettings)
        val response = client.requestDiscuss(discussRequest)
        Assertions.assertNotNull(response.candidates.firstOrNull())
    }
}
