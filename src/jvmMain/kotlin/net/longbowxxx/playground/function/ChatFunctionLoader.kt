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

    fun loadFunctions(directory: File): List<OpenAiChatFunction> {
        return directory.walkTopDown().filter {
            it.isFile && it.name.endsWith(".json")
        }.map { file ->
            file.readText(Charsets.UTF_8).let { jsonString ->
                decodeJson.decodeFromString<OpenAiChatFunction>(jsonString)
            }
        }.toList()
    }
}
