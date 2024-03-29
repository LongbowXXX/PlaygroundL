/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.function

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.longbowxxx.openai.client.OpenAiChatFunction
import net.longbowxxx.openai.client.OpenAiChatParameter
import net.longbowxxx.openai.client.OpenAiChatProperty
import net.longbowxxx.openai.client.OpenAiClient
import net.longbowxxx.openai.client.OpenAiCreateImageRequest
import net.longbowxxx.openai.client.OpenAiSettings
import net.longbowxxx.playground.utils.copyTo
import net.longbowxxx.playground.utils.randomShortId
import net.longbowxxx.playground.utils.toURL
import net.longbowxxx.playground.viewmodel.appProperties
import java.io.File

class CreateImageFunctionPlugin(private val appDataDir: File) : ChatFunctionPlugin() {
    override val functionSpec: OpenAiChatFunction
        get() =
            OpenAiChatFunction(
                "create_image",
                "Generate images using DALL·E 3",
                OpenAiChatParameter.OpenAiChatParameterObject(
                    mapOf(
                        "image_name" to
                            OpenAiChatProperty(
                                "string",
                                "the name of the image to generate in English. Ex: tokyo_tower",
                            ),
                        "prompt" to
                            OpenAiChatProperty(
                                "string",
                                "the prompt for image generation in English. Ex: Tokyo tower.",
                            ),
                    ),
                    listOf("prompt", "image_name"),
                ),
            )

    override suspend fun executeInternal(arguments: String): String {
        val imageArgs = arguments.toParams<CreateImageArgs>()
        val client = OpenAiClient(OpenAiSettings(appProperties.apiKey))
        val request =
            OpenAiCreateImageRequest.ofDallE3(
                imageArgs.prompt,
            )
        val response = client.requestCreateImage(request)

        val imageUris =
            response.data.mapNotNull { imageData -> imageData.url?.toURL() }
                .map { imageUrl ->
                    // 画像をファイルに保存する
                    val shortId = randomShortId()
                    val imageName = "${imageArgs.imageName}-$shortId.png"
                    val logDir = File(appDataDir, "log/images")
                    val file = File(logDir, imageName)
                    file.parentFile.mkdirs()
                    imageUrl.copyTo(file)
                    file.toURI().toASCIIString()
                }

        return CreateImageResponse(SUCCESS, imageUris).toResponseStr()
    }
}

@Serializable
data class CreateImageArgs(
    val prompt: String,
    @SerialName("image_name")
    val imageName: String,
)

@Serializable
data class CreateImageResponse(
    val type: String,
    @SerialName("image_uris")
    val imageUris: List<String>,
)
