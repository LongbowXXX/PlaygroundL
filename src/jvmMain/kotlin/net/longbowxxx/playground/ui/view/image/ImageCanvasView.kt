/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.image

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Suppress("FunctionName")
@Composable
fun ImageCanvasView() {
    var canvasSize by remember { mutableStateOf(IntSize(0, 0)) }
    // Modifier.size(1024.dp, 1024.dp)
    Canvas(
        modifier = Modifier.fillMaxSize().aspectRatio(1f, true)
            .onSizeChanged { canvasSize = it },
    ) {
        // ここに描画コードを追加する
        drawLine(
            Color.Black,
            Offset(0f, 0f),
            Offset(canvasSize.width.toFloat(), canvasSize.height.toFloat()),
            strokeWidth = 12.dp.toPx(),
        )
    }
}
