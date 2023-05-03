/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.longbowxxx.playground.ui.widget.ErrorWidget
import net.longbowxxx.playground.viewmodel.appProperties
import net.longbowxxx.playground.viewmodel.imageViewModel

private const val PROMPT_LABEL_TEXT = "PROMPT IN ENGLISH"

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
@Preview
fun ImageScreen() {
    var prompt by remember { imageViewModel.prompt }
    val responseImage by remember { imageViewModel.responseImage }
    val requesting by remember { imageViewModel.requesting }
    val verticalScrollState = rememberScrollState(0)
    var errorMessage by remember { imageViewModel.errorMessage }
    val messageFontSizeSp by remember { appProperties.messageFontSizeSp }

    Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        TextField(
            prompt,
            {
                prompt = it
            },
            label = {
                Text(PROMPT_LABEL_TEXT)
            },
            modifier = Modifier.fillMaxHeight().verticalScroll(verticalScrollState).weight(0.5f),
            readOnly = requesting,
            textStyle = TextStyle(fontSize = messageFontSizeSp.sp),
        )
        LaunchedEffect(prompt.length) {
            verticalScrollState.scrollTo(verticalScrollState.maxValue)
        }

        // 画像を表示する
        Box(
            modifier = Modifier.weight(1f),
        ) {
            responseImage?.asComposeImageBitmap()?.let { imageBitmap ->
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Image",
                    modifier = Modifier.fillMaxSize(),
                    // 画像のサイズを指定する
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
    ErrorWidget(errorMessage) { errorMessage = "" }
}
