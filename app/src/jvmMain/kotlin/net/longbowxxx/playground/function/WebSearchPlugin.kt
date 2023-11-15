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
import net.longbowxxx.playground.viewmodel.appProperties
import net.longbowxxx.search.GoogleCustomSearchClientSettings
import net.longbowxxx.search.SearchClient
import net.longbowxxx.search.SearchRequest

class WebSearchPlugin : ChatFunctionPlugin() {
    override val functionSpec: OpenAiChatFunction
        get() =
            OpenAiChatFunction(
                "search_web",
                "search for information from the web",
                OpenAiChatParameter.OpenAiChatParameterObject(
                    mapOf(
                        "query" to OpenAiChatProperty("string", "query for search"),
                    ),
                    listOf("query"),
                ),
            )

    override suspend fun executeInternal(arguments: String): String {
        val param = arguments.toParams<WebSearchArgs>()
        val searchResult =
            withContext(Dispatchers.IO) {
                val client =
                    SearchClient(
                        GoogleCustomSearchClientSettings(
                            appProperties.googleApiKey,
                            appProperties.googleCustomSearchCx,
                        ),
                    )
                client.search(SearchRequest(param.query))
            }
        return WebSearchResponse(
            SUCCESS,
            searchResult.results.map {
                WebSearchResult(it.link, it.title, it.snippet)
            },
        ).toResponseStr()
    }
}

@Serializable
data class WebSearchArgs(
    val query: String,
)

@Serializable
data class WebSearchResponse(
    val type: String,
    val results: List<WebSearchResult>,
)

@Serializable
data class WebSearchResult(
    val link: String,
    val title: String,
    val snippet: String,
)
