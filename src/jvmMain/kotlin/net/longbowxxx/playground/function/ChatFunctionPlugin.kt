/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.function

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.longbowxxx.openai.client.OpenAiChatFunction
import net.longbowxxx.playground.history.ChatHistory
import net.longbowxxx.playground.utils.DebugLoggable
import net.longbowxxx.playground.utils.logTrace
import java.io.File

abstract class ChatFunctionPlugin : DebugLoggable {
    companion object {
        const val SUCCESS = "success"
    }

    protected val decodeJson = Json {
        encodeDefaults = false
        ignoreUnknownKeys = true
    }

    protected val encodeJson = Json {
        encodeDefaults = false
        prettyPrint = true
    }

    protected inline fun <reified T> String.toParams(): T {
        return decodeJson.decodeFromString<T>(this)
    }

    abstract val functionSpec: OpenAiChatFunction
    suspend fun execute(arguments: String, context: FunctionCallContext): String {
        return runCatching {
            logTrace { "execute(): arguments=$arguments, context=$context" }
            executeInternal(arguments, context)
        }.getOrElse { ex ->
            "failed. exception=$ex"
        }.also {
            logTrace { "execute(): result=$it" }
        }
    }

    protected abstract suspend fun executeInternal(arguments: String, context: FunctionCallContext): String
}

data class FunctionCallContext(
    val session: ChatHistory.ChatHistorySession,
    val logDir: File,
)
