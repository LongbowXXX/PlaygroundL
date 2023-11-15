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

class MockFunctionPlugin(override val functionSpec: OpenAiChatFunction, private val response: String) : ChatFunctionPlugin() {
    companion object {
        private const val FUNCTION_FILE = "function.json"
        private const val RESPONSE_FILE = "response.json"
        private val decodeJson =
            Json {
                encodeDefaults = false
                ignoreUnknownKeys = true
            }

        fun isMockPlugin(directory: File): Boolean {
            return File(directory, FUNCTION_FILE).exists() && File(directory, RESPONSE_FILE).exists()
        }

        fun loadMockPlugin(directory: File): ChatFunctionPlugin {
            val functionFile = File(directory, FUNCTION_FILE)
            val responseFile = File(directory, RESPONSE_FILE)

            val spec =
                functionFile.readText(Charsets.UTF_8).let { jsonString ->
                    decodeJson.decodeFromString<OpenAiChatFunction>(jsonString)
                }
            val response = responseFile.readText(Charsets.UTF_8)
            return MockFunctionPlugin(spec, response)
        }
    }

    override suspend fun executeInternal(arguments: String): String {
        return response
    }
}
