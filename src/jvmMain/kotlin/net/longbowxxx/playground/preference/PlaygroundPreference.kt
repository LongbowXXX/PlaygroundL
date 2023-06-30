/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.preference

import androidx.compose.runtime.mutableStateOf

class PlaygroundPreference : PreferenceBase() {
    companion object {
        private const val API_KEY_KEY = "apikey.encrypted"
        private const val PALM_API_KEY_KEY = "palm.apikey.encrypted"
        private const val GOOGLE_API_KEY_KEY = "google.apikey.encrypted"
        private const val GOOGLE_CUSTOM_SEARCH_CX_KEY = "google.customsearch.cx.encrypted"
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
    val palmApiKeyEnabled = mutableStateOf(false)
    val googleApiKeyEnabled = mutableStateOf(false)
    val googleCustomSearchCxEnabled = mutableStateOf(false)
    val messageFontSizeSp = mutableStateOf(DEFAULT_MESSAGE_FONT_SIZE)
    var windowLeft: Int = DEFAULT_WINDOW_LEFT
    var windowTop: Int = DEFAULT_WINDOW_TOP
    var windowWidth: Int = DEFAULT_WINDOW_WIDTH
    var windowHeight: Int = DEFAULT_WINDOW_HEIGHT
    override val fileName = FILE_NAME
    override val fileComment = FILE_COMMENT

    var apiKey: String
        set(value) {
            properties.setSecretProperty(API_KEY_KEY, value)
            apiKeyEnabled.value = value.isNotEmpty()
        }
        get() = properties.getOrDefaultSecretProperty(API_KEY_KEY, "")

    var palmApiKey: String
        set(value) {
            properties.setSecretProperty(PALM_API_KEY_KEY, value)
            palmApiKeyEnabled.value = value.isNotEmpty()
        }
        get() = properties.getOrDefaultSecretProperty(PALM_API_KEY_KEY, "")

    var googleApiKey: String
        set(value) {
            properties.setSecretProperty(GOOGLE_API_KEY_KEY, value)
            googleApiKeyEnabled.value = value.isNotEmpty()
        }
        get() = properties.getOrDefaultSecretProperty(GOOGLE_API_KEY_KEY, "")

    var googleCustomSearchCx: String
        set(value) {
            properties.setSecretProperty(GOOGLE_CUSTOM_SEARCH_CX_KEY, value)
            googleCustomSearchCxEnabled.value = value.isNotEmpty()
        }
        get() = properties.getOrDefaultSecretProperty(GOOGLE_CUSTOM_SEARCH_CX_KEY, "")

    override fun load() {
        loadInternal {
            apiKeyEnabled.value = apiKey.isNotEmpty()
            palmApiKeyEnabled.value = palmApiKey.isNotEmpty()
            googleApiKeyEnabled.value = googleApiKey.isNotEmpty()
            googleCustomSearchCxEnabled.value = googleCustomSearchCx.isNotEmpty()
            messageFontSizeSp.value = getIntProperty(MESSAGE_FONT_SIZE_KEY, DEFAULT_MESSAGE_FONT_SIZE)
            windowLeft = getIntProperty(WINDOW_LEFT_KEY, DEFAULT_WINDOW_LEFT)
            windowTop = getIntProperty(WINDOW_TOP_KEY, DEFAULT_WINDOW_TOP)
            windowWidth = getIntProperty(WINDOW_WIDTH_KEY, DEFAULT_WINDOW_WIDTH)
            windowHeight = getIntProperty(WINDOW_HEIGHT_KEY, DEFAULT_WINDOW_HEIGHT)
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

    fun resetPalmApiKey() {
        palmApiKey = ""
    }

    fun resetGoogleApiKey() {
        googleApiKey = ""
    }

    fun resetCustomSearchCxKey() {
        googleCustomSearchCx = ""
    }

    override fun save() {
        saveInternal {
            setProperty(MESSAGE_FONT_SIZE_KEY, messageFontSizeSp.value.toString())
            setProperty(WINDOW_LEFT_KEY, windowLeft.toString())
            setProperty(WINDOW_TOP_KEY, windowTop.toString())
            setProperty(WINDOW_WIDTH_KEY, windowWidth.toString())
            setProperty(WINDOW_HEIGHT_KEY, windowHeight.toString())
        }
    }
}
