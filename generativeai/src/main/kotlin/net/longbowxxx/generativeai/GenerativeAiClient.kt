/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.generativeai

interface GenerativeAiClient {
    companion object {
        operator fun invoke(settings: GenerativeAiSettings): GenerativeAiClient {
            return GenerativeAiClientImpl(settings)
        }
    }

    suspend fun requestDiscuss()
}

data class GenerativeAiSettings(
    val apiKey: String,
)
