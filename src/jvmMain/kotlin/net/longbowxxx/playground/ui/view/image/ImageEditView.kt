/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
    val selectedImageIndex by remember { imageViewModel.selectedImageIndex }
    val responseImages by remember { imageViewModel.responseImages }

    Column(modifier = Modifier.weight(1f).fillMaxWidth()) {
        if (selectedImageIndex >= 0) {
            val image = responseImages[selectedImageIndex]
            Image(
                bitmap = image.first.asComposeImageBitmap(),
                contentDescription = "Image-$selectedImageIndex",
                modifier = Modifier.fillMaxWidth().padding(10.dp).weight(1f),
                // 画像のサイズを指定する
                contentScale = ContentScale.Fit,
            )
            Button(
                { imageViewModel.requestImageVariation() },
            ) {
                Text(CREATE_VARIATION_TEXT)
            }
        }
    }
}
