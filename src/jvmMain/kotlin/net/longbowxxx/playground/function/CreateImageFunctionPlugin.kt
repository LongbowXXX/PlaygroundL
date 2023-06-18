/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.function

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import net.longbowxxx.openai.client.OpenAiChatFunction
import net.longbowxxx.openai.client.OpenAiChatParameter
import net.longbowxxx.openai.client.OpenAiChatProperty
import net.longbowxxx.openai.client.OpenAiClient
import net.longbowxxx.openai.client.OpenAiCreateImageRequest
import net.longbowxxx.openai.client.OpenAiSettings
import net.longbowxxx.playground.utils.copyTo
import net.longbowxxx.playground.utils.toURL
import net.longbowxxx.playground.viewmodel.appProperties
import java.io.File

class CreateImageFunctionPlugin : ChatFunctionPlugin() {
    override val functionSpec: OpenAiChatFunction
        get() = OpenAiChatFunction(
            "create_image",
            "Generate images using DALL·E 2",
            OpenAiChatParameter.OpenAiChatParameterObject(
                mapOf(
                    "image_name" to OpenAiChatProperty(
                        "string",
                        "the name of the image to generate in English. Ex: tokyo_tower",
                    ),
                    "prompt" to OpenAiChatProperty(
                        "string",
                        "the prompt for image generation in English. Ex: Tokyo tower.",
                    ),
                    "number_of_images" to OpenAiChatProperty("integer", "number of images to generate"),
                ),
                listOf("prompt", "image_name", "number_of_images"),
            ),
        )

    override suspend fun executeInternal(arguments: String, context: FunctionCallContext): String {
        val imageArgs = arguments.toParams<CreateImageArgs>()
        val client = OpenAiClient(OpenAiSettings(appProperties.apiKey))
        val request = OpenAiCreateImageRequest(
            imageArgs.prompt,
            n = imageArgs.numberOfImages,
        )
        val response = client.requestCreateImage(request)

        val imageUrls = response.data.mapNotNull { imageData -> imageData.url?.toURL() }
            .mapIndexed { index, imageUrl ->
                // 画像をファイルに保存する
                val imageName = "${imageArgs.imageName}-$index.png"
                val file = File(context.logDir, imageName)
                imageUrl.copyTo(file)
                imageUrl.toString()
            }

        return encodeJson.encodeToString(CreateImageResponse(SUCCESS, imageUrls))
    }
}

@Serializable
data class CreateImageArgs(
    val prompt: String,
    @SerialName("image_name")
    val imageName: String,
    @SerialName("number_of_images")
    val numberOfImages: Int,
)

@Serializable
data class CreateImageResponse(
    val type: String,
    @SerialName("image_urls")
    val imageUrls: List<String>,
)
