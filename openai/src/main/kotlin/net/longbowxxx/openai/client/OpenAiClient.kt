/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.openai.client

import kotlinx.coroutines.flow.Flow

interface OpenAiClient {
    companion object {
        operator fun invoke(settings: OpenAiSettings): OpenAiClient {
            return OpenAiClientImpl(settings)
        }
    }

    fun requestChatWithStreaming(request: OpenAiChatRequest): Flow<OpenAiChatStreamResponse>
    suspend fun requestCreateImage(request: OpenAiCreateImageRequest): OpenAiImageResponse
    suspend fun requestEditImage(request: OpenAiEditImageRequest): OpenAiImageResponse
}

data class OpenAiSettings(
    val baseUrl: String,
    val apiKey: String,
)
