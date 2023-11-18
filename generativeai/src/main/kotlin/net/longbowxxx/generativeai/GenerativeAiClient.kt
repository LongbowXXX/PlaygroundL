/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.generativeai

/**
 * Generative AI client.
 */
interface GenerativeAiClient {
    companion object {
        operator fun invoke(settings: GenerativeAiSettings): GenerativeAiClient {
            return GenerativeAiClientImpl(settings)
        }
    }

    /**
     * Request discuss.
     *
     * @param discussRequest Request parameters.
     * @return Response.
     */
    suspend fun requestDiscuss(discussRequest: DiscussRequest): DiscussResponse
}

/**
 * Generative AI client settings.
 *
 * @property apiKey API key.
 */
data class GenerativeAiSettings(
    val apiKey: String,
)
