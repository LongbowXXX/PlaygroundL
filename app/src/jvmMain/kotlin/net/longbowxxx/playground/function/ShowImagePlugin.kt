/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.function

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.longbowxxx.openai.client.OpenAiChatFunction
import net.longbowxxx.openai.client.OpenAiChatParameter
import net.longbowxxx.openai.client.OpenAiChatProperty

class ShowImagePlugin : ChatFunctionPlugin() {
    override val functionSpec: OpenAiChatFunction
        get() =
            OpenAiChatFunction(
                "show_image",
                "show image",
                OpenAiChatParameter.OpenAiChatParameterObject(
                    mapOf(
                        "image_uri" to OpenAiChatProperty("string", "URI to show"),
                    ),
                    listOf("image_uri"),
                ),
            )

    override suspend fun executeInternal(arguments: String): String {
        return withContext(Dispatchers.IO) {
            val imageArgs = arguments.toParams<ShowImageArgs>()
            ShowImageResponse(SUCCESS, imageArgs.imageUri).toResponseStr()
        }
    }
}

@Serializable
data class ShowImageArgs(
    @SerialName("image_uri")
    val imageUri: String,
)

@Serializable
data class ShowImageResponse(
    val type: String,
    @SerialName("image_uri")
    val imageUri: String,
)
