/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.function

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.longbowxxx.openai.client.OpenAiChatFunction
import net.longbowxxx.playground.utils.DebugLoggable
import net.longbowxxx.playground.utils.logError
import net.longbowxxx.playground.utils.logTrace

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

    @Suppress("FunctionName")
    @Composable
    fun FunctionView(content: String) {
        runCatching {
            FunctionViewInternal(content)
        }.onFailure {
            logError(it) { "FunctionView failure" }
        }
    }

    @Suppress("FunctionName")
    @Composable
    protected open fun FunctionViewInternal(content: String) {
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
