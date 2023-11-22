/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.openai.client.function

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.longbowxxx.openai.client.OpenAiChatFunction
import net.longbowxxx.utils.DebugLoggable
import net.longbowxxx.utils.logError
import net.longbowxxx.utils.logTrace

/**
 * This is the base class for all chat function plugins.
 * It provides common functionality for all chat function plugins.
 * It implements the DebugLoggable interface.
 */
abstract class ChatFunctionPlugin : DebugLoggable {
    companion object {
        const val SUCCESS = "success"
    }

    /**
     * This is a Json object used for decoding.
     * It is configured to not encode defaults and ignore unknown keys.
     */
    protected val decodeJson =
        Json {
            encodeDefaults = false
            ignoreUnknownKeys = true
        }

    /**
     * This is a Json object used for encoding.
     * It is configured to not encode defaults and pretty print the output.
     */
    protected val encodeJson =
        Json {
            encodeDefaults = false
            prettyPrint = true
        }

    /**
     * This function converts a string to a specified type.
     * It uses the decodeJson object for decoding.
     */
    protected inline fun <reified T> String.toParams(): T {
        return decodeJson.decodeFromString<T>(this)
    }

    /**
     * This is the specification of the chat function.
     * It is abstract and must be implemented by subclasses.
     */
    abstract val functionSpec: OpenAiChatFunction

    /**
     * This function executes the chat function.
     * It logs the arguments and result, and handles errors.
     * It calls the executeInternal function which must be implemented by subclasses.
     */
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

    /**
     * This function executes the chat function.
     * It is abstract and must be implemented by subclasses.
     */
    protected abstract suspend fun executeInternal(arguments: String): String

    /**
     * This function converts a specified type to a string.
     * It uses the encodeJson object for encoding.
     */
    protected inline fun <reified T> T.toResponseStr(): String {
        return encodeJson.encodeToString(this)
    }

    /**
     * This data class represents an error response.
     * It includes the type of response and the reason for the error.
     */
    @Serializable
    data class ErrorResponse(
        val type: String,
        val reason: String,
    )
}
