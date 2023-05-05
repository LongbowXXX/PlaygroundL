/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.image

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Suppress("FunctionName")
@Composable
fun ImageCanvasView() {
    var canvasSize by remember { mutableStateOf(IntSize(0, 0)) }
    var drawingLine by remember { mutableStateOf(listOf<Offset>()) }
    var drawLines by remember { mutableStateOf(listOf<List<Offset>>()) }

    Canvas(
        modifier = Modifier.fillMaxSize().aspectRatio(1f, true)
            .onSizeChanged { canvasSize = it }
            .pointerInput(Unit) {
                // マウスドラッグ中の処理
                detectDragGestures(
                    onDragStart = { startOffset ->
                        drawingLine = drawingLine.addOffset(startOffset)
                    },
                    onDragEnd = {
                        drawLines = drawLines.addLine(drawingLine)
                        drawingLine = emptyList()
                    },
                ) { change, dragAmount ->
                    change.consume()
                    drawingLine = drawingLine.addOffset(dragAmount)
                }
            },
    ) {
        drawLine(drawingLine, canvasSize)
        drawLines.forEach {
            drawLine(it, canvasSize)
        }
    }
}

private fun DrawScope.drawLine(line: List<Offset>, canvasSize: IntSize) {
    if (line.size >= 2) {
        line.reduce { acc, offset ->
            val endPoint = acc + offset
            if (canvasSize.hit(acc) && canvasSize.hit(endPoint)) {
                drawLine(
                    Color.Black,
                    acc,
                    endPoint,
                    strokeWidth = 12.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }
            endPoint
        }
    }
}

private fun IntSize.hit(offset: Offset): Boolean {
    return 0 <= offset.x && offset.x <= width && 0 <= offset.y && offset.y <= height
}

private fun List<Offset>.addOffset(offset: Offset): List<Offset> {
    return this.toMutableList().apply {
        add(offset)
    }
}

private fun List<List<Offset>>.addLine(line: List<Offset>): List<List<Offset>> {
    return this.toMutableList().apply {
        add(line)
    }
}
