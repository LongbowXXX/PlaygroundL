/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.logger

import kotlinx.serialization.encodeToString
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRequest

class ChatLogger(
    parentDir: String,
) : LoggerBase(parentDir) {

    suspend fun logRequest(request: OpenAiChatRequest) {
        writeLog {
            write("Playground Chat Log $dateTimeStr\n")
            write("===\n")
            write("# Request Parameters\n")
            write("```json\n")
            val json = encodeJson.encodeToString(request.extractSettings())
            write("$json\n")
            write("```\n")
            write(HORIZONTAL_LINE)
        }
    }

    suspend fun logMessages(messages: List<OpenAiChatMessage>) {
        writeLog {
            messages.forEach { message ->
                write("# ${message.role.name}\n")
                write("${message.content}\n")
                write(HORIZONTAL_LINE)
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
