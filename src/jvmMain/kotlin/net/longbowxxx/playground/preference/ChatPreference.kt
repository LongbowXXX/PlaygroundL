/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.preference

import androidx.compose.runtime.mutableStateOf
import net.longbowxxx.openai.client.CHAT_FREQUENCY_PENALTY_DEFAULT
import net.longbowxxx.openai.client.CHAT_MAX_TOKENS_DEFAULT
import net.longbowxxx.openai.client.CHAT_PRESENCE_PENALTY_DEFAULT
import net.longbowxxx.openai.client.CHAT_TEMPERATURE_DEFAULT
import net.longbowxxx.openai.client.CHAT_TOP_P_DEFAULT
import net.longbowxxx.openai.client.OPENAI_CHAT_MODEL_GPT_35_TURBO
import java.io.File
import java.util.*

class ChatPreference : PreferenceBase() {
    companion object {
        private const val CHAT_REQUEST_TEMPERATURE_KEY = "chat.request.temperature"
        private const val CHAT_REQUEST_TOP_P_KEY = "chat.request.topP"
        private const val CHAT_REQUEST_MAX_TOKENS_KEY = "chat.request.maxTokens"
        private const val CHAT_REQUEST_PRESENCE_PENALTY_KEY = "chat.request.presencePenalty"
        private const val CHAT_REQUEST_FREQUENCY_PENALTY_KEY = "chat.request.frequencyPenalty"
        private const val CHAT_REQUEST_MODEL_KEY = "chat.request.model"
        private const val CHAT_SYSTEM_PROMPT_KEY = "chat.system.prompt"
        private const val FILE_NAME = "chat.properties"
        private const val FILE_COMMENT = "Playground Chat Properties"
    }

    val chatTemperature = mutableStateOf(CHAT_TEMPERATURE_DEFAULT)
    val chatTopP = mutableStateOf(CHAT_TOP_P_DEFAULT)
    val chatMaxTokens = mutableStateOf(CHAT_MAX_TOKENS_DEFAULT)
    val chatPresencePenalty = mutableStateOf(CHAT_PRESENCE_PENALTY_DEFAULT)
    val chatFrequencyPenalty = mutableStateOf(CHAT_FREQUENCY_PENALTY_DEFAULT)
    val chatSystemPrompt = mutableStateOf("")
    val chatModel = mutableStateOf(OPENAI_CHAT_MODEL_GPT_35_TURBO)

    private val properties = Properties()

    fun load() {
        runCatching {
            File(FILE_NAME).reader(Charsets.UTF_8).use { reader ->
                properties.load(reader)
            }
            chatTemperature.value = properties.getFloatProperty(CHAT_REQUEST_TEMPERATURE_KEY, CHAT_TEMPERATURE_DEFAULT)
            chatTopP.value = properties.getFloatProperty(CHAT_REQUEST_TOP_P_KEY, CHAT_TOP_P_DEFAULT)
            chatMaxTokens.value = properties.getIntProperty(CHAT_REQUEST_MAX_TOKENS_KEY, CHAT_MAX_TOKENS_DEFAULT)
            chatPresencePenalty.value =
                properties.getFloatProperty(CHAT_REQUEST_PRESENCE_PENALTY_KEY, CHAT_PRESENCE_PENALTY_DEFAULT)
            chatFrequencyPenalty.value =
                properties.getFloatProperty(CHAT_REQUEST_FREQUENCY_PENALTY_KEY, CHAT_FREQUENCY_PENALTY_DEFAULT)
            chatSystemPrompt.value = properties.getProperty(CHAT_SYSTEM_PROMPT_KEY, "")
            chatModel.value = properties.getProperty(CHAT_REQUEST_MODEL_KEY, OPENAI_CHAT_MODEL_GPT_35_TURBO)
        }.onFailure {
            save()
        }
    }

    fun reset() {
        chatModel.value = OPENAI_CHAT_MODEL_GPT_35_TURBO
        chatTemperature.value = CHAT_TEMPERATURE_DEFAULT
        chatTopP.value = CHAT_TOP_P_DEFAULT
        chatMaxTokens.value = CHAT_MAX_TOKENS_DEFAULT
        chatPresencePenalty.value = CHAT_PRESENCE_PENALTY_DEFAULT
        chatFrequencyPenalty.value = CHAT_FREQUENCY_PENALTY_DEFAULT
    }

    private fun save() {
        properties.setProperty(CHAT_REQUEST_TEMPERATURE_KEY, chatTemperature.value.toString())
        properties.setProperty(CHAT_REQUEST_TOP_P_KEY, chatTopP.value.toString())
        properties.setProperty(CHAT_REQUEST_MAX_TOKENS_KEY, chatMaxTokens.value.toString())
        properties.setProperty(CHAT_REQUEST_PRESENCE_PENALTY_KEY, chatPresencePenalty.value.toString())
        properties.setProperty(CHAT_REQUEST_FREQUENCY_PENALTY_KEY, chatFrequencyPenalty.value.toString())
        properties.setProperty(CHAT_SYSTEM_PROMPT_KEY, chatSystemPrompt.value)
        properties.setProperty(CHAT_REQUEST_MODEL_KEY, chatModel.value)
        File(FILE_NAME).writer(Charsets.UTF_8).use { writer ->
            properties.store(writer, FILE_COMMENT)
        }
    }

    override fun close() {
        save()
    }
}
