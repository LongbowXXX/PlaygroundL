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
import net.longbowxxx.openai.client.OPENAI_CHAT_MODEL_GPT_35_TURBO_0613

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
    val chatModel = mutableStateOf(OPENAI_CHAT_MODEL_GPT_35_TURBO_0613)

    override val fileName = FILE_NAME
    override val fileComment = FILE_COMMENT

    override fun load() {
        loadInternal {
            chatTemperature.value = getFloatProperty(CHAT_REQUEST_TEMPERATURE_KEY, CHAT_TEMPERATURE_DEFAULT)
            chatTopP.value = getFloatProperty(CHAT_REQUEST_TOP_P_KEY, CHAT_TOP_P_DEFAULT)
            chatMaxTokens.value = getIntProperty(CHAT_REQUEST_MAX_TOKENS_KEY, CHAT_MAX_TOKENS_DEFAULT)
            chatPresencePenalty.value =
                getFloatProperty(CHAT_REQUEST_PRESENCE_PENALTY_KEY, CHAT_PRESENCE_PENALTY_DEFAULT)
            chatFrequencyPenalty.value =
                getFloatProperty(CHAT_REQUEST_FREQUENCY_PENALTY_KEY, CHAT_FREQUENCY_PENALTY_DEFAULT)
            chatSystemPrompt.value = getProperty(CHAT_SYSTEM_PROMPT_KEY, "")
            chatModel.value = getProperty(CHAT_REQUEST_MODEL_KEY, OPENAI_CHAT_MODEL_GPT_35_TURBO_0613)
        }
    }

    fun reset() {
        chatModel.value = OPENAI_CHAT_MODEL_GPT_35_TURBO_0613
        chatTemperature.value = CHAT_TEMPERATURE_DEFAULT
        chatTopP.value = CHAT_TOP_P_DEFAULT
        chatMaxTokens.value = CHAT_MAX_TOKENS_DEFAULT
        chatPresencePenalty.value = CHAT_PRESENCE_PENALTY_DEFAULT
        chatFrequencyPenalty.value = CHAT_FREQUENCY_PENALTY_DEFAULT
    }

    override fun save() {
        saveInternal {
            setProperty(CHAT_REQUEST_TEMPERATURE_KEY, chatTemperature.value.toString())
            setProperty(CHAT_REQUEST_TOP_P_KEY, chatTopP.value.toString())
            setProperty(CHAT_REQUEST_MAX_TOKENS_KEY, chatMaxTokens.value.toString())
            setProperty(CHAT_REQUEST_PRESENCE_PENALTY_KEY, chatPresencePenalty.value.toString())
            setProperty(CHAT_REQUEST_FREQUENCY_PENALTY_KEY, chatFrequencyPenalty.value.toString())
            setProperty(CHAT_SYSTEM_PROMPT_KEY, chatSystemPrompt.value)
            setProperty(CHAT_REQUEST_MODEL_KEY, chatModel.value)
        }
    }
}
