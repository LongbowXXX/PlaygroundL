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
import net.longbowxxx.playground.viewmodel.appProperties
import kotlin.math.roundToInt

private const val OPENAI_API_KEY_TEXT = "OPENAI_API_KEY"
private const val RESET_OPENAI_API_KEY_TEXT = "RESET OPENAI API KEY"
private const val MESSAGE_FONT_SIZE_TEXT = "MESSAGE FONT SIZE"

@Suppress("FunctionName")
@Composable
fun AppParamsView() {
    val apikeyEnabled by remember { appProperties.apiKeyEnabled }
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

    TextSlider(
        "$MESSAGE_FONT_SIZE_TEXT : $messageFontSizeSp",
        messageFontSizeSp.toFloat(),
        12f..32f,
    ) {
        messageFontSizeSp = it.roundToInt()
    }
}
