/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.viewmodel.imageViewModel

private const val CREATE_VARIATION_TEXT = "CREATE VARIATION"

@Suppress("FunctionName")
@Composable
fun ColumnScope.ImageEditView() {
    val activeImage by remember { imageViewModel.activeImage }
    val requesting by remember { imageViewModel.requesting }

    Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(10.dp).weight(1f)
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
        Button(
            { imageViewModel.requestImageVariation() },
            enabled = activeImage != null && !requesting,
        ) {
            Text(CREATE_VARIATION_TEXT)
        }
    }
}
