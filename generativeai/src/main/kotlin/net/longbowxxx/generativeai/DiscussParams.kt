/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.generativeai

const val DISCUSS_MODEL = "models/chat-bison-001"

data class DiscussExample(
    val input: DiscussMessage,
    val output: DiscussMessage,
)

data class DiscussMessage(
    val content: String,
    val author: String = "",
)

data class DiscussPrompt(
    val messages: List<DiscussMessage>,
    val examples: List<DiscussExample> = emptyList(),
    val context: String = "",
)

data class DiscussRequest(
    val model: String,
    val prompt: DiscussPrompt,
    val temperature: Float,
    val candidateCount: Int,
)

data class DiscussResponse(
    val candidates: List<DiscussMessage>,
)
