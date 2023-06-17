/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.generativeai

import kotlinx.serialization.Serializable

const val DISCUSS_MODEL = "models/chat-bison-001"
const val DISCUSS_TEMPERATURE_DEFAULT = 0.5f

@Serializable
data class DiscussExample(
    val input: DiscussMessage,
    val output: DiscussMessage,
)

@Serializable
data class DiscussMessage(
    val author: String = "",
    val content: String,
)

@Serializable
data class DiscussPrompt(
    val messages: List<DiscussMessage>,
    val examples: List<DiscussExample> = emptyList(),
    val context: String = "",
)

@Serializable
data class DiscussRequest(
    val model: String,
    val prompt: DiscussPrompt,
    val temperature: Float,
    val candidateCount: Int,
)

@Serializable
data class DiscussResponse(
    val candidates: List<DiscussMessage>,
)
