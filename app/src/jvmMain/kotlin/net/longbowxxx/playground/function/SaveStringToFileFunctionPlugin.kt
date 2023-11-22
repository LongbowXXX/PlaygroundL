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
import net.longbowxxx.openai.client.function.ChatFunctionPlugin
import net.longbowxxx.utils.logTrace
import net.longbowxxx.utils.randomShortId
import java.io.File

/**
 * This class is a plugin for the chat function that saves a string to a file.
 * It extends the ChatFunctionPlugin class.
 */
class SaveStringToFileFunctionPlugin : ChatFunctionPlugin() {
    /**
     * This property defines the specification of the chat function.
     * It includes the function name, description, and parameters.
     */
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

    /**
     * This function executes the chat function.
     * It saves the string data to a file.
     * The file name and data are passed as arguments.
     * It returns a response string.
     */
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

/**
 * This data class represents the arguments for the save string to file function.
 * It includes the file name and data to be saved.
 */
@Serializable
data class SaveStringToFileArgs(
    @SerialName("file_name")
    val fileName: String,
    val data: String,
)

/**
 * This data class represents the response of the save string to file function.
 * It includes the type of response and the URI of the saved file.
 */
@Serializable
data class SaveStringToFileResponse(
    val type: String,
    @SerialName("saved_file_uri")
    val savedFileUri: String,
)
