/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun ChatParamsWidget() {
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

@Composable
fun TextSlider(
    title: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onChanged: (Float) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            title,
            fontSize = 12.sp,
            modifier = Modifier.width(200.dp),
        )
        Slider(
            value,
            { onChanged(it) },
            valueRange = valueRange,
            steps = 100,
        )
    }
}

private fun Float.roundToSecond(): Float {
    return (this * 100.0f).roundToInt() / 100.0f
}
