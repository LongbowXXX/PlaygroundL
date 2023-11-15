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
import net.longbowxxx.playground.utils.randomShortId
import java.io.File

class SaveStringToFileFunctionPlugin : ChatFunctionPlugin() {
    override val functionSpec: OpenAiChatFunction
        get() =
            OpenAiChatFunction(
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

    override suspend fun executeInternal(arguments: String): String {
        val param = arguments.toParams<SaveStringToFileArgs>()
        val file =
            withContext(Dispatchers.IO) {
                val shortId = randomShortId()
                File("log/save", "${param.fileName}-$shortId").also {
                    logTrace { "save to file. ${it.absolutePath}" }
                }.apply {
                    writeText(param.data, Charsets.UTF_8)
                }
            }
        return SaveStringToFileResponse(SUCCESS, file.toURI().toASCIIString()).toResponseStr()
    }
}

@Serializable
data class SaveStringToFileArgs(
    @SerialName("file_name")
    val fileName: String,
    val data: String,
)

@Serializable
data class SaveStringToFileResponse(
    val type: String,
    @SerialName("saved_file_uri")
    val savedFileUri: String,
)
