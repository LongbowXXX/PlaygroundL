/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.search

import java.io.PrintStream

private const val ENV_GOOGLE_API_KEY = "GOOGLE_API_KEY"
private const val ENV_GOOGLE_SEARCH_CX_KEY = "GOOGLE_SEARCH_Cx"

val googleApiKey: String by lazy {
    requireNotNull(System.getenv(ENV_GOOGLE_API_KEY)) { "Needs $ENV_GOOGLE_API_KEY on system envelopment." }
}

val googleApiKeyEnvEnabled: Boolean by lazy {
    System.getenv(ENV_GOOGLE_API_KEY) != null
}

val googleSearchCx: String by lazy {
    requireNotNull(System.getenv(ENV_GOOGLE_SEARCH_CX_KEY)) { "Needs $ENV_GOOGLE_SEARCH_CX_KEY on system envelopment." }
}

val googleSearchCxEnvEnabled: Boolean by lazy {
    System.getenv(ENV_GOOGLE_SEARCH_CX_KEY) != null
}

val googleCustomSearchClientSettings: GoogleCustomSearchClientSettings by lazy {
    GoogleCustomSearchClientSettings(googleApiKey, googleSearchCx)
}

inline fun logSearchRequest(lazyMessage: () -> Any?) {
    logSearchRequestStream?.println("${lazyMessage()}")
}

var logSearchRequestStream: PrintStream? = System.out

inline fun logSearchResponse(lazyMessage: () -> Any?) {
    logSearchResponseStream?.println("${lazyMessage()}")
}

var logSearchResponseStream: PrintStream? = System.out
