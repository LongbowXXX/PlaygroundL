/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.preference

import androidx.compose.runtime.mutableStateOf
import java.io.File
import java.util.*

class PlaygroundPreference : PreferenceBase() {
    companion object {
        private const val API_KEY_KEY = "apikey.encrypted"
        private const val MESSAGE_FONT_SIZE_KEY = "message.font.size"
        private const val WINDOW_LEFT_KEY = "window.left"
        private const val WINDOW_TOP_KEY = "window.top"
        private const val WINDOW_WIDTH_KEY = "window.width"
        private const val WINDOW_HEIGHT_KEY = "window.height"
        private const val FILE_NAME = "playgroundApp.properties"
        private const val FILE_COMMENT = "Playground App Properties"
        private const val DEFAULT_MESSAGE_FONT_SIZE = 16
        private const val DEFAULT_WINDOW_LEFT = 100
        private const val DEFAULT_WINDOW_TOP = 100
        private const val DEFAULT_WINDOW_WIDTH = 1280
        private const val DEFAULT_WINDOW_HEIGHT = 1024
    }

    val apiKeyEnabled = mutableStateOf(false)
    val messageFontSizeSp = mutableStateOf(DEFAULT_MESSAGE_FONT_SIZE)
    var windowLeft: Int = DEFAULT_WINDOW_LEFT
    var windowTop: Int = DEFAULT_WINDOW_TOP
    var windowWidth: Int = DEFAULT_WINDOW_WIDTH
    var windowHeight: Int = DEFAULT_WINDOW_HEIGHT
    private val properties = Properties()

    var apiKey: String
        set(value) {
            properties.setSecretProperty(API_KEY_KEY, value)
            apiKeyEnabled.value = value.isNotEmpty()
        }
        get() = properties.getOrDefaultSecretProperty(API_KEY_KEY, "")

    fun load() {
        runCatching {
            File(FILE_NAME).reader(Charsets.UTF_8).use { reader ->
                properties.load(reader)
            }
            apiKeyEnabled.value = apiKey.isNotEmpty()
            messageFontSizeSp.value = properties.getIntProperty(MESSAGE_FONT_SIZE_KEY, DEFAULT_MESSAGE_FONT_SIZE)
            windowLeft = properties.getIntProperty(WINDOW_LEFT_KEY, DEFAULT_WINDOW_LEFT)
            windowTop = properties.getIntProperty(WINDOW_TOP_KEY, DEFAULT_WINDOW_TOP)
            windowWidth = properties.getIntProperty(WINDOW_WIDTH_KEY, DEFAULT_WINDOW_WIDTH)
            windowHeight = properties.getIntProperty(WINDOW_HEIGHT_KEY, DEFAULT_WINDOW_HEIGHT)
        }.onFailure {
            save()
        }
    }

    fun updateWindowState(left: Int, top: Int, width: Int, height: Int) {
        windowLeft = left
        windowTop = top
        windowWidth = width
        windowHeight = height
    }

    fun resetApiKey() {
        apiKey = ""
    }

    private fun save() {
        properties.setProperty(MESSAGE_FONT_SIZE_KEY, messageFontSizeSp.value.toString())
        properties.setProperty(WINDOW_LEFT_KEY, windowLeft.toString())
        properties.setProperty(WINDOW_TOP_KEY, windowTop.toString())
        properties.setProperty(WINDOW_WIDTH_KEY, windowWidth.toString())
        properties.setProperty(WINDOW_HEIGHT_KEY, windowHeight.toString())
        File(FILE_NAME).writer(Charsets.UTF_8).use { writer ->
            properties.store(writer, FILE_COMMENT)
        }
    }

    override fun close() {
        save()
    }
}
