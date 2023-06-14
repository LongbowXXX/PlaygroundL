/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.openai.client

import java.io.PrintStream

private const val ENV_OPENAI_API_KEY = "OPENAI_API_KEY"

val openAiApiKey: String by lazy {
    requireNotNull(System.getenv(ENV_OPENAI_API_KEY)) { "Needs $ENV_OPENAI_API_KEY on system envelopment." }
}

val openAiApiKeyEnvEnabled: Boolean by lazy {
    System.getenv(ENV_OPENAI_API_KEY) != null
}

val openAiSettings: OpenAiSettings by lazy {
    OpenAiSettings(openAiApiKey)
}

inline fun logOpenAiRequest(lazyMessage: () -> Any?) {
    logOpenAiRequestStream?.println("${lazyMessage()}")
}

var logOpenAiRequestStream: PrintStream? = null

inline fun logOpenAiResponse(lazyMessage: () -> Any?) {
    logOpenAiResponseStream?.println("${lazyMessage()}")
}

var logOpenAiResponseStream: PrintStream? = null
