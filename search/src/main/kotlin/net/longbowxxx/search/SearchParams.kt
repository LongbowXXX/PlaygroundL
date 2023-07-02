/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.search

data class SearchRequest(
    val text: String,
)

data class SearchResponse(
    val results: List<SearchResult>,
)

data class SearchResult(
    val link: String,
    val title: String,
    val snippet: String,
)
