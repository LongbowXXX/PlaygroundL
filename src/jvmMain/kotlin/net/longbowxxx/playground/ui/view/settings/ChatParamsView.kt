/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.ui.widget.ModelSelectorWidget
import net.longbowxxx.playground.ui.widget.TextSlider
import net.longbowxxx.playground.ui.widget.roundToSecond
import net.longbowxxx.playground.viewmodel.chatProperties
import kotlin.math.roundToInt

private const val TEMPERATURE_TEXT = "TEMPERATURE"
private const val TOP_P_TEXT = "TOP P"
private const val MAX_TOKENS_TEXT = "MAX TOKENS"
private const val PRESENCE_TEXT = "PRESENCE PENALTY"
private const val FREQUENCY_TEXT = "FREQUENCY PENALTY"
private const val RESET_TEXT = "RESET CHAT PARAMS"

@Suppress("FunctionName")
@Composable
fun ChatParamsView() {
    var temperature by remember { chatProperties.chatTemperature }
    var topP by remember { chatProperties.chatTopP }
    var maxTokens by remember { chatProperties.chatMaxTokens }
    var presencePenalty by remember { chatProperties.chatPresencePenalty }
    var frequencyPenalty by remember { chatProperties.chatFrequencyPenalty }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ModelSelectorWidget()
        TextSlider(
            "$TEMPERATURE_TEXT : $temperature",
            temperature,
            0f..1f,
        ) {
            temperature = it.roundToSecond()
        }
        TextSlider(
            "$TOP_P_TEXT : $topP",
            topP,
            0f..1f,
        ) {
            topP = it.roundToSecond()
        }
        TextSlider(
            "$MAX_TOKENS_TEXT : $maxTokens",
            maxTokens.toFloat(),
            1f..2048f,
        ) {
            maxTokens = it.roundToInt()
        }
        TextSlider(
            "$PRESENCE_TEXT : $presencePenalty",
            presencePenalty,
            0f..2f,
        ) {
            presencePenalty = it.roundToSecond()
        }
        TextSlider(
            "$FREQUENCY_TEXT : $frequencyPenalty",
            frequencyPenalty,
            0f..2f,
        ) {
            frequencyPenalty = it.roundToSecond()
        }
        Button(
            { chatProperties.reset() },
        ) {
            Text(RESET_TEXT)
        }
    }
}
