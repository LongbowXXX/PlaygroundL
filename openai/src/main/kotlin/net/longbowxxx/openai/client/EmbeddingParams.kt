/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.openai.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val EMBEDDING_MODEL_ADA_002 = "text-embedding-ada-002"

@Serializable
data class OpenAiEmbeddingRequest(
    val input: String,
    val model: String,
)

@Serializable
data class OpenAiEmbeddingResponse(
    val data: List<OpenAiEmbeddingData>,
    val model: String,
    @SerialName("object")
    val objectType: String,
    val usage: OpenAiEmbeddingUsage,
)

@Serializable
data class OpenAiEmbeddingData(
    val embedding: List<Float>,
    val index: Int,
    @SerialName("object")
    val objectType: String,
)

@Serializable
data class OpenAiEmbeddingUsage(
    @SerialName("prompt_tokens")
    val promptTokens: Int,
    @SerialName("total_tokens")
    val totalTokens: Int,
)
