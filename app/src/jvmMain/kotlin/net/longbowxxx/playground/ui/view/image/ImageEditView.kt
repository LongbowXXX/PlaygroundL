/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.ui.widget.TextSlider
import net.longbowxxx.playground.viewmodel.imageViewModel
import kotlin.math.roundToInt

private const val CREATE_VARIATION_TEXT = "CREATE VARIATION"
private const val EDIT_IMAGE_TEXT = "EDIT IMAGE"
private const val CLEAR_MASK_TEXT = "CLEAR MASK"

private const val STROKE_WIDTH_TEXT = "STROKE WIDTH"

@Suppress("FunctionName")
@Composable
fun ColumnScope.ImageEditView() {
    val activeImage by remember { imageViewModel.activeImage }
    val requesting by remember { imageViewModel.requesting }
    var maskStrokeWidth by remember { imageViewModel.maskStrokeWidth }

    Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
        Box(
            modifier =
                Modifier.fillMaxWidth().padding(10.dp).weight(1f)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(10.dp)),
        ) {
            activeImage?.let { image ->
                Image(
                    bitmap = image.first.asComposeImageBitmap(),
                    contentDescription = image.second.name,
                    modifier = Modifier.fillMaxSize().padding(10.dp),
                    contentScale = ContentScale.Fit,
                )
            }
            ImageCanvasView()
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                { imageViewModel.requestImageVariation() },
                enabled = activeImage != null && !requesting,
            ) {
                Text(CREATE_VARIATION_TEXT)
            }
            Button(
                { imageViewModel.requestEditImage() },
                enabled = activeImage != null && !requesting,
            ) {
                Text(EDIT_IMAGE_TEXT)
            }
            TextButton(
                { imageViewModel.clearMaskImage() },
                enabled = !requesting,
            ) {
                Icon(Icons.Default.Delete, null)
                Text(CLEAR_MASK_TEXT)
            }

            TextSlider(
                "$STROKE_WIDTH_TEXT : $maskStrokeWidth",
                maskStrokeWidth,
                6f..32f,
            ) {
                maskStrokeWidth = it.roundToInt().toFloat()
            }
        }
    }
}
