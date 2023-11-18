/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.search

/**
 * Search client.
 */
interface SearchClient {
    companion object {
        operator fun invoke(settings: GoogleCustomSearchClientSettings): GoogleCustomSearchClient {
            return GoogleCustomSearchClient(settings)
        }
    }

    /**
     * Search.
     *
     * @param request Request parameters.
     * @return Response.
     */
    suspend fun search(request: SearchRequest): SearchResponse
}
