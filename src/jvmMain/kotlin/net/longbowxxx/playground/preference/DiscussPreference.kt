/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.preference

import androidx.compose.runtime.mutableStateOf
import net.longbowxxx.generativeai.DISCUSS_MODEL
import net.longbowxxx.generativeai.DISCUSS_TEMPERATURE_DEFAULT

class DiscussPreference : PreferenceBase() {
    companion object {
        private const val DISCUSS_REQUEST_TEMPERATURE_KEY = "discuss.request.temperature"
        private const val DISCUSS_REQUEST_MODEL_KEY = "discuss.request.model"
        private const val DISCUSS_SYSTEM_PROMPT_KEY = "discuss.system.prompt"
        private const val FILE_NAME = "discuss.properties"
        private const val FILE_COMMENT = "Playground Discuss Properties"
    }

    val discussTemperature = mutableStateOf(DISCUSS_TEMPERATURE_DEFAULT)
    val discussSystemPrompt = mutableStateOf("")
    val discussModel = mutableStateOf(DISCUSS_MODEL)

    override val fileName = FILE_NAME
    override val fileComment = FILE_COMMENT

    override fun load() {
        loadInternal {
            discussTemperature.value = getFloatProperty(DISCUSS_REQUEST_TEMPERATURE_KEY, DISCUSS_TEMPERATURE_DEFAULT)
            discussSystemPrompt.value = getProperty(DISCUSS_SYSTEM_PROMPT_KEY, "")
            discussModel.value = getProperty(DISCUSS_REQUEST_MODEL_KEY, DISCUSS_MODEL)
        }
    }

    fun reset() {
        discussModel.value = DISCUSS_MODEL
        discussTemperature.value = DISCUSS_TEMPERATURE_DEFAULT
    }

    override fun save() {
        saveInternal {
            setProperty(DISCUSS_REQUEST_TEMPERATURE_KEY, discussTemperature.value.toString())
            setProperty(DISCUSS_SYSTEM_PROMPT_KEY, discussSystemPrompt.value)
            setProperty(DISCUSS_REQUEST_MODEL_KEY, discussModel.value)
        }
    }
}
