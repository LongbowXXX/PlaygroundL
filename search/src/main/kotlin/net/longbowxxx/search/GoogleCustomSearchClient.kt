/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.search

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.customsearch.v1.CustomSearchAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Google Custom Search client.
 *
 * @constructor
 * @param settings Settings.
 */
class GoogleCustomSearchClient(
    private val settings: GoogleCustomSearchClientSettings,
) : SearchClient {
    /**
     * Search.
     *
     * @param request Request parameters.
     * @return Response.
     */
    override suspend fun search(request: SearchRequest): SearchResponse {
        return withContext(Dispatchers.IO) {
            logSearchRequest { "search() : request=$request" }
            val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
            val jsonFactory = GsonFactory.getDefaultInstance()

            val customsearch =
                CustomSearchAPI.Builder(httpTransport, jsonFactory, null)
                    // .setApplicationName("TestProject")
                    .build()

            val list = customsearch.cse().list().setCx(settings.cx).setKey(settings.apiKey).setQ(request.text).execute().items
            val results =
                list.map {
                    SearchResult(
                        it.link,
                        it.title,
                        it.snippet,
                    )
                }
            SearchResponse((results))
        }.also {
            logSearchResponse { "search() : response=$it" }
        }
    }
}

/**
 * Google Custom Search client settings.
 *
 * @property apiKey API key.
 * @property cx Custom search engine ID.
 */
data class GoogleCustomSearchClientSettings(
    val apiKey: String,
    val cx: String,
)
