/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.openai.client

import kotlinx.coroutines.flow.Flow

/**
 * OpenAI client.
 */
interface OpenAiClient {
    companion object {
        operator fun invoke(settings: OpenAiSettings): OpenAiClient {
            return OpenAiClientImpl(settings)
        }
    }

    /**
     * Request chat.
     *
     * @param request Request parameters.
     * @return Response.
     */
    suspend fun requestChatWithStreaming(request: OpenAiChatRequest): Flow<OpenAiChatStreamResponse>

    /**
     * Request image creation.
     *
     * @param request Request parameters.
     * @return Response.
     */
    suspend fun requestCreateImage(request: OpenAiCreateImageRequest): OpenAiImageResponse

    /**
     * Request image editing.
     *
     * @param request Request parameters.
     * @return Response.
     */
    suspend fun requestEditImage(request: OpenAiEditImageRequest): OpenAiImageResponse

    /**
     * Request image variation.
     *
     * @param request Request parameters.
     * @return Response.
     */
    suspend fun requestImageVariation(request: OpenAiImageVariationRequest): OpenAiImageResponse

    /**
     * Request audio transcription.
     *
     * @param request Request parameters.
     * @return Response.
     */
    suspend fun requestAudioTranscription(request: OpenAiAudioRequest): OpenAiAudioResponse

    /**
     * Request audio translation.
     *
     * @param request Request parameters.
     * @return Response.
     */
    suspend fun requestAudioTranslation(request: OpenAiAudioRequest): OpenAiAudioResponse

    /**
     * Request embedding.
     *
     * @param request Request parameters.
     * @return Response.
     */
    suspend fun requestEmbedding(request: OpenAiEmbeddingRequest): OpenAiEmbeddingResponse
}

/**
 * OpenAI client settings.
 *
 * @property apiKey API key.
 */
data class OpenAiSettings(
    val apiKey: String,
)
