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
import java.io.File

class ChatFunctionLoader {
    private val decodeJson = Json {
        encodeDefaults = false
        ignoreUnknownKeys = true
    }

    private val plugins = listOf(
        SaveStringToFileFunctionPlugin(),
    )

    fun loadPlugins(directory: File): List<ChatFunctionPlugin> {
        val allPlugins = directory.walkTopDown().filter {
            it.isFile && it.name.endsWith(".json")
        }.map { file ->
            file.readText(Charsets.UTF_8).let { jsonString ->
                val spec = decodeJson.decodeFromString<OpenAiChatFunction>(jsonString)
                MockFunctionPlugin(spec)
            }
        }.toMutableList<ChatFunctionPlugin>()
        allPlugins.addAll(
            plugins,
        )
        return allPlugins
    }
}
