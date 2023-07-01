/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.function

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import net.longbowxxx.openai.client.OpenAiChatFunction
import net.longbowxxx.openai.client.OpenAiChatParameter
import net.longbowxxx.openai.client.OpenAiChatProperty
import java.io.File
import java.net.URI

class ReadDataPlugin : ChatFunctionPlugin() {
    override val functionSpec: OpenAiChatFunction
        get() = OpenAiChatFunction(
            "read_data_from_uri",
            "read data from URI",
            OpenAiChatParameter.OpenAiChatParameterObject(
                mapOf(
                    "dataUris" to OpenAiChatProperty(
                        OpenAiChatProperty.ARRAY_TYPE,
                        "URIs to read",
                        null,
                        OpenAiChatProperty(OpenAiChatProperty.STRING_TYPE),
                    ),
                ),
                listOf("dataUris"),
            ),
        )

    override suspend fun executeInternal(arguments: String): String {
        val param = arguments.toParams<ReadDataArgs>()
        val texts = withContext(Dispatchers.IO) {
            param.dataUris.map { uriString ->
                File(URI(uriString)).readText(Charsets.UTF_8).let { text ->
                    uriString to text
                }
            }
        }
        return ReadDataResponse(
            SUCCESS,
            texts.map {
                ReadDataResult(it.first, it.second)
            },
        ).toResponseStr()
    }
}

@Serializable
data class ReadDataArgs(
    val dataUris: List<String>,
)

@Serializable
data class ReadDataResponse(
    val type: String,
    val results: List<ReadDataResult>,
)

@Serializable
data class ReadDataResult(
    val dataUri: String,
    val text: String,
)
