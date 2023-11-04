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
import org.jsoup.Jsoup
import java.net.HttpURLConnection
import java.net.URL

class ReadWebPlugin : ChatFunctionPlugin() {
    override val functionSpec: OpenAiChatFunction
        get() =
            OpenAiChatFunction(
                "read_web_from_url",
                "read web from URL",
                OpenAiChatParameter.OpenAiChatParameterObject(
                    mapOf(
                        "dataUrls" to
                            OpenAiChatProperty(
                                OpenAiChatProperty.ARRAY_TYPE,
                                "URLs to read",
                                null,
                                OpenAiChatProperty(OpenAiChatProperty.STRING_TYPE),
                            ),
                    ),
                    listOf("dataUrls"),
                ),
            )

    override suspend fun executeInternal(arguments: String): String {
        val param = arguments.toParams<ReadWebArgs>()
        val texts =
            withContext(Dispatchers.IO) {
                param.dataUrls.map { urlString ->
                    val url = URL(urlString)
                    val urlConnection = url.openConnection() as HttpURLConnection

                    try {
                        val htmlText = urlConnection.inputStream.bufferedReader().readText()
                        val planeText = Jsoup.parse(htmlText).text()
                        urlString to planeText
                    } finally {
                        urlConnection.disconnect()
                    }
                }
            }
        return ReadWebResponse(
            SUCCESS,
            texts.map {
                ReadWebResult(it.first, it.second)
            },
        ).toResponseStr()
    }
}

@Serializable
data class ReadWebArgs(
    val dataUrls: List<String>,
)

@Serializable
data class ReadWebResponse(
    val type: String,
    val results: List<ReadWebResult>,
)

@Serializable
data class ReadWebResult(
    val dataUrl: String,
    val text: String,
)
