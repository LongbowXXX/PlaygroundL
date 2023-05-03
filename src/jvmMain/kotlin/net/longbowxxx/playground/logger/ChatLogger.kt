/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.logger

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRequest

class ChatLogger(
    parentDir: String,
) : LoggerBase(parentDir) {

    suspend fun logRequest(request: OpenAiChatRequest) {
        withContext(Dispatchers.IO) {
            writer.write("Playground Chat Log $dateTimeStr\n")
            writer.write("===\n")
            writer.write("# Request Parameters\n")
            writer.write("```json\n")
            val json = encodeJson.encodeToString(request.extractSettings())
            writer.write("$json\n")
            writer.write("```\n")
            writer.write(HORIZONTAL_LINE)
        }
    }

    suspend fun logMessages(messages: List<OpenAiChatMessage>) {
        withContext(Dispatchers.IO) {
            messages.forEach { message ->
                writer.write("# ${message.role.name}\n")
                writer.write("${message.content}\n")
                writer.write(HORIZONTAL_LINE)
            }
        }
    }

    private fun OpenAiChatRequest.extractSettings(): OpenAiChatRequest {
        return OpenAiChatRequest(
            model,
            emptyList(),
            temperature,
            topP,
            n,
            false,
            stop,
            maxTokens,
            presencePenalty,
            frequencyPenalty,
            logicBias,
        )
    }
}
