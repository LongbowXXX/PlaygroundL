/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

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

fun Float.roundToSecond(): Float {
    return (this * 100.0f).roundToInt() / 100.0f
}
