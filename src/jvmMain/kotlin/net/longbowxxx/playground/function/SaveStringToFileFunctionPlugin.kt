/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.function

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.longbowxxx.openai.client.OpenAiChatFunction
import net.longbowxxx.openai.client.OpenAiChatParameter
import net.longbowxxx.openai.client.OpenAiChatProperty
import net.longbowxxx.playground.utils.logTrace
import java.io.File

class SaveStringToFileFunctionPlugin : ChatFunctionPlugin() {
    override val functionSpec: OpenAiChatFunction
        get() = OpenAiChatFunction(
            "save_string_to_file",
            "save string data to a file",
            OpenAiChatParameter.OpenAiChatParameterObject(
                mapOf(
                    "file_name" to OpenAiChatProperty("string", "File name to save data"),
                    "data" to OpenAiChatProperty("string", "Data to save to file"),
                ),
                listOf("file_name", "data"),
            ),
        )

    override suspend fun executeInternal(arguments: String, context: FunctionCallContext): String {
        val param = arguments.toParams<SaveStringToFileArgs>()
        withContext(Dispatchers.IO) {
            File(context.logDir, param.fileName).also {
                logTrace { "save to file. ${it.absolutePath}" }
            }.writeText(param.data, Charsets.UTF_8)
        }
        return SUCCESS
    }
}

@Serializable
data class SaveStringToFileArgs(
    @SerialName("file_name")
    val fileName: String,
    val data: String,
)
