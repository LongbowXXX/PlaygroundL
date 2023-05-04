/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.image

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.viewmodel.imageViewModel

@Suppress("FunctionName")
@Composable
fun ColumnScope.ImageListView() {
    val responseImages by remember { imageViewModel.responseImages }
    var selectedImageIndex by remember { imageViewModel.selectedImageIndex }
    val listState = rememberLazyListState()

    // 画像を表示する
    Box(
        modifier = Modifier.weight(0.5f).fillMaxWidth(),
    ) {
        LazyRow(
            modifier = Modifier.padding(10.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(10.dp)),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = listState,
        ) {
            itemsIndexed(responseImages) { index, image ->
                Image(
                    bitmap = image.asComposeImageBitmap(),
                    contentDescription = "Image-$index",
                    modifier = Modifier.fillMaxSize().padding(10.dp).clickable { selectedImageIndex = index },
                    // 画像のサイズを指定する
                    contentScale = ContentScale.Fit,
                )
            }
        }
        LaunchedEffect(responseImages.size) {
            if (responseImages.isNotEmpty()) {
                // リストに要素が追加された場合に末尾にスクロール
                listState.scrollToItem(index = responseImages.size - 1)
            }
        }
        HorizontalScrollbar(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            adapter = rememberScrollbarAdapter(scrollState = listState),
        )
    }
}
