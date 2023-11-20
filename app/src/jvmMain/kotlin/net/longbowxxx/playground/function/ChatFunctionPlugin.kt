/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.function

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.longbowxxx.openai.client.OpenAiChatFunction
import net.longbowxxx.utils.DebugLoggable
import net.longbowxxx.utils.logError
import net.longbowxxx.utils.logTrace

/**
 * Chat function plugin base class.
 *
 * @see OpenAiChatFunction
 */
abstract class ChatFunctionPlugin : DebugLoggable {
    companion object {
        const val SUCCESS = "success"
    }

    protected val decodeJson =
        Json {
            encodeDefaults = false
            ignoreUnknownKeys = true
        }

    protected val encodeJson =
        Json {
            encodeDefaults = false
            prettyPrint = true
        }

    protected inline fun <reified T> String.toParams(): T {
        return decodeJson.decodeFromString<T>(this)
    }

    /**
     * Function spec.
     */
    abstract val functionSpec: OpenAiChatFunction

    suspend fun execute(arguments: String): String {
        return runCatching {
            logTrace { "execute(): arguments=$arguments" }
            executeInternal(arguments)
        }.getOrElse { ex ->
            logError(ex) { "execute() failed." }
            ErrorResponse("error", "$ex").toResponseStr()
        }.also {
            logTrace { "execute(): result=$it" }
        }
    }

    protected abstract suspend fun executeInternal(arguments: String): String

    protected inline fun <reified T> T.toResponseStr(): String {
        return encodeJson.encodeToString(this)
    }

    @Serializable
    data class ErrorResponse(
        val type: String,
        val reason: String,
    )
}
