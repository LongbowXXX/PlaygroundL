/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.logger

import kotlinx.serialization.encodeToString
import net.longbowxxx.generativeai.DiscussMessage
import net.longbowxxx.generativeai.DiscussPrompt
import net.longbowxxx.generativeai.DiscussRequest

class DiscussLogger(
    parentDir: String = DISCUSS_LOG_DIR,
) : LoggerBase(parentDir) {

    companion object {
        const val DISCUSS_LOG_DIR = "$LOG_DIR/discuss"
    }

    suspend fun logRequest(request: DiscussRequest) {
        writeLog {
            write("Playground Chat Log $dateTimeStr\n")
            write("===\n")
            write("# Request Parameters\n")
            write("```json\n")
            val json = encodeJson.encodeToString(request.extractSettings())
            write("$json\n")
            write("```\n")
            appendHorizontalLine()
        }
    }

    suspend fun logMessages(messages: List<DiscussMessage>) {
        writeLog {
            messages.forEach { message ->
                write("# ${message.author}\n")
                write("${message.content}\n")
                appendHorizontalLine()
            }
        }
    }

    private fun DiscussRequest.extractSettings(): DiscussRequest {
        return DiscussRequest(
            model,
            DiscussPrompt(
                emptyList(),
                emptyList(),
                prompt.context,
            ),
            temperature,
            candidateCount,
        )
    }
}
