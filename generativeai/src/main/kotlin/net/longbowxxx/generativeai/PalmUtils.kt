/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.generativeai

import java.io.PrintStream

private const val ENV_PALM_API_KEY = "PALM_API_KEY"

val palmApiKey: String by lazy {
    requireNotNull(System.getenv(ENV_PALM_API_KEY)) { "Needs $ENV_PALM_API_KEY on system envelopment." }
}

val palmApiKeyEnvEnabled: Boolean by lazy {
    System.getenv(ENV_PALM_API_KEY) != null
}

val generativeAiSettings: GenerativeAiSettings by lazy {
    GenerativeAiSettings(palmApiKey)
}

inline fun logGenerativeAiRequest(lazyMessage: () -> Any?) {
    logGenerativeAiRequestStream?.println("${lazyMessage()}")
}

var logGenerativeAiRequestStream: PrintStream? = System.out

inline fun logGenerativeAiResponse(lazyMessage: () -> Any?) {
    logGenerativeAiResponseStream?.println("${lazyMessage()}")
}

var logGenerativeAiResponseStream: PrintStream? = System.out
