/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.image

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.viewmodel.imageViewModel
import org.jetbrains.skiko.toImage
import java.awt.BasicStroke
import java.awt.BasicStroke.CAP_ROUND
import java.awt.BasicStroke.JOIN_ROUND
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import kotlin.math.roundToInt

@Suppress("FunctionName")
@Composable
fun ImageCanvasView() {
    var canvasSize by remember { mutableStateOf(IntSize(1024, 1024)) }
    var drawingLine by remember { mutableStateOf(listOf<Offset>()) }
    var drawLines by remember { mutableStateOf(listOf<List<Offset>>()) }

    var maskImage by remember {
        imageViewModel.maskImage
    }

    // Image への描画
    maskImage.createGraphics().apply {
        // 描画設定
        color = java.awt.Color.BLACK
        stroke = BasicStroke(12f, CAP_ROUND, JOIN_ROUND)

        // 線を描画
        drawLine(drawingLine)
        drawLines.forEach {
            drawLine(it)
        }
    }.dispose()

    // ImageIO.write(bufferedImage, "png", File("tmpImage.png"))

    Image(
        bitmap = maskImage.toImage().toComposeImageBitmap(),
        contentDescription = null,
        modifier = Modifier.fillMaxSize().padding(10.dp).aspectRatio(1f, true)
            .onSizeChanged {
                canvasSize = it
                maskImage = BufferedImage(it.width, it.height, BufferedImage.TYPE_INT_ARGB)
            }
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
            }
            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary)),
    )
}

private fun Graphics2D.drawLine(line: List<Offset>) {
    if (line.size >= 2) {
        line.reduce { acc, offset ->
            val endPoint = acc + offset
            drawLine(
                acc.x.roundToInt(),
                acc.y.roundToInt(),
                endPoint.x.roundToInt(),
                endPoint.y.roundToInt(),
            )
            endPoint
        }
    }
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
