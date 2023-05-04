package net.longbowxxx.openai.client

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.net.URL

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
        val createImageRequest = OpenAiCreateImageRequest(
            "two lady\nmanga",
            responseFormat = OpenAiImageResponseFormatTypes.URL,
        )
        val createResponse = client.requestCreateImage(createImageRequest)
        println("$createResponse")
        val createdImageFile = File("created_image.png")
        URL(createResponse.data[0].url).openStream().use { input ->
            createdImageFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val editImageRequest = OpenAiEditImageRequest(
            createdImageFile,
            null,
            "photo real",
        )
        val editResponse = client.requestEditImage(editImageRequest)
        println("$editResponse")
        val editImageFile = File("edit_image.png")
        URL(editResponse.data[0].url).openStream().use { input ->
            editImageFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        Unit
    }
}
