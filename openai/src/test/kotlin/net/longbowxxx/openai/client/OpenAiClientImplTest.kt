package net.longbowxxx.openai.client

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.net.URL

@OptIn(ExperimentalCoroutinesApi::class)
class OpenAiClientImplTest {
    @Disabled
    @Test
    fun requestWithStreaming() =
        runBlocking {
            val client = OpenAiClient(openAiSettings)
            val request =
                OpenAiChatRequest(
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
    fun requestWithFunction() =
        runBlocking {
            logOpenAiRequestStream = System.out
            logOpenAiResponseStream = System.out
            val weatherFunc =
                OpenAiChatFunction(
                    name = "get_current_weather",
                    description = "Get the current weather in a given location",
                    parameters =
                        OpenAiChatParameter.OpenAiChatParameterObject(
                            mapOf(
                                "location" to OpenAiChatProperty("string", "The city and state, e.g. San Francisco, CA"),
                                "unit" to OpenAiChatProperty("string", null, listOf("celsius", "fahrenheit")),
                            ),
                            required = listOf("location"),
                        ),
                )

            val client = OpenAiClient(openAiSettings)
            val request =
                OpenAiChatRequest(
                    "gpt-3.5-turbo-0613",
                    listOf(
                        OpenAiChatMessage(OpenAiChatRoleTypes.ASSISTANT, "今日の東京の天気教えて"),
                    ),
                    listOf(weatherFunc),
                    functionCall = ofFunctionCallAuto(),
                    stream = true,
                )
            client.requestChatWithStreaming(request).collect {
                println("$it")
            }
        }

    @Disabled
    @Test
    fun requestCreateImage() =
        runBlocking {
            val client = OpenAiClient(openAiSettings)
            val createImageRequest =
                OpenAiCreateImageRequest(
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

            val editImageRequest =
                OpenAiEditImageRequest(
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

    @Disabled
    @Test
    fun requestEmbedding() =
        runTest {
            val client = OpenAiClient(openAiSettings)
            val response =
                client.requestEmbedding(
                    OpenAiEmbeddingRequest(
                        model = EMBEDDING_MODEL_ADA_002,
                        input = "おはようございます。",
                    ),
                )
            println("$response")
        }
}
