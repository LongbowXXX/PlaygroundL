/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.settings

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import net.longbowxxx.playground.ui.widget.SecretTextInputWidget
import net.longbowxxx.playground.ui.widget.TextSlider
import net.longbowxxx.playground.viewmodel.appProperties
import kotlin.math.roundToInt

private const val OPENAI_API_KEY_TEXT = "OPENAI_API_KEY"
private const val RESET_OPENAI_API_KEY_TEXT = "RESET OPENAI API KEY"
private const val PALM_API_KEY_TEXT = "PALM_API_KEY"
private const val RESET_PALM_API_KEY_TEXT = "RESET PALM API KEY"
private const val GOOGLE_API_KEY_TEXT = "GOOGLE_API_KEY"
private const val RESET_GOOGLE_API_KEY_TEXT = "RESET GOOGLE API KEY"
private const val GOOGLE_CUSTOM_SEARCH_CX_TEXT = "GOOGLE_CUSTOM_SEARCH_CX"
private const val RESET_CUSTOM_SEARCH_CX_TEXT = "RESET CUSTOM SEARCH CX"
private const val MESSAGE_FONT_SIZE_TEXT = "MESSAGE FONT SIZE"

@Suppress("FunctionName")
@Composable
fun AppParamsView() {
    val apikeyEnabled by remember { appProperties.apiKeyEnabled }
    val palmApiKeyEnabled by remember { appProperties.palmApiKeyEnabled }
    val googleApiKeyEnabled by remember { appProperties.googleApiKeyEnabled }
    val googleCustomSearchCxEnabled by remember { appProperties.googleCustomSearchCxEnabled }
    var messageFontSizeSp by remember { appProperties.messageFontSizeSp }

    if (!apikeyEnabled) {
        SecretTextInputWidget(
            OPENAI_API_KEY_TEXT,
            false,
        ) { appProperties.apiKey = it }
    } else {
        Button(
            { appProperties.resetApiKey() },
        ) {
            Text(RESET_OPENAI_API_KEY_TEXT)
        }
    }

    if (!palmApiKeyEnabled) {
        SecretTextInputWidget(
            PALM_API_KEY_TEXT,
            false,
        ) { appProperties.palmApiKey = it }
    } else {
        Button(
            { appProperties.resetPalmApiKey() },
        ) {
            Text(RESET_PALM_API_KEY_TEXT)
        }
    }

    if (!googleApiKeyEnabled) {
        SecretTextInputWidget(
            GOOGLE_API_KEY_TEXT,
            false,
        ) { appProperties.googleApiKey = it }
    } else {
        Button(
            { appProperties.resetGoogleApiKey() },
        ) {
            Text(RESET_GOOGLE_API_KEY_TEXT)
        }
    }

    if (!googleCustomSearchCxEnabled) {
        SecretTextInputWidget(
            GOOGLE_CUSTOM_SEARCH_CX_TEXT,
            false,
        ) { appProperties.googleCustomSearchCx = it }
    } else {
        Button(
            { appProperties.resetCustomSearchCxKey() },
        ) {
            Text(RESET_CUSTOM_SEARCH_CX_TEXT)
        }
    }

    TextSlider(
        "$MESSAGE_FONT_SIZE_TEXT : $messageFontSizeSp",
        messageFontSizeSp.toFloat(),
        12f..32f,
    ) {
        messageFontSizeSp = it.roundToInt()
    }
}
