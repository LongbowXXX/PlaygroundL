/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget.function

import androidx.compose.runtime.Composable
import kotlinx.serialization.json.Json
import net.longbowxxx.openai.client.OpenAiChatFunction
import net.longbowxxx.openai.client.function.ChatFunctionPlugin
import net.longbowxxx.utils.DebugLoggable
import net.longbowxxx.utils.logError

abstract class ChatFunctionPluginItemView(
    parent: ChatFunctionPlugin,
) : DebugLoggable {
    val functionSpec: OpenAiChatFunction = parent.functionSpec
    protected val decodeJson =
        Json {
            encodeDefaults = false
            ignoreUnknownKeys = true
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
    protected abstract fun FunctionViewInternal(content: String)

    protected inline fun <reified T> String.toParams(): T {
        return decodeJson.decodeFromString<T>(this)
    }
}
