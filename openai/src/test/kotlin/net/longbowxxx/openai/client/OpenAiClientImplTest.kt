package net.longbowxxx.openai.client

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class OpenAiClientImplTest {

    @Disabled
    @Test
    fun requestWithStreaming() = runBlocking {
        val client = OpenAiClient(openAiSettings)
        val request = OpenAiChatRequest(
            "gpt-3.5-turbo",
            listOf(
                OpenAiChatMessage(OpenAiChatRoleTypes.ASSISTANT, "こんにちは"),
            ),
            stream = true,
        )
        client.requestChatWithStreaming(request).collect {
            println("$it")
        }
    }

    @Disabled
    @Test
    fun requestCreateImage() = runBlocking {
        val client = OpenAiClient(openAiSettings)
        val request = OpenAiCreateImageRequest(
            "女性の絵",
            responseFormat = OpenAiImageResponseFormatTypes.B64_JSON,
        )
        val response = client.requestCreateImage(request)
        println("$response")
    }
}
