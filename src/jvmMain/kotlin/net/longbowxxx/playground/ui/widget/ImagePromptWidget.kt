/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import net.longbowxxx.playground.viewmodel.appProperties
import net.longbowxxx.playground.viewmodel.imageViewModel

private const val PROMPT_LABEL_TEXT = "PROMPT IN ENGLISH"

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun RowScope.ImagePromptWidget() {
    var prompt by remember { imageViewModel.prompt }
    val verticalScrollState = rememberScrollState(0)
    val requesting by remember { imageViewModel.requesting }
    val messageFontSizeSp by remember { appProperties.messageFontSizeSp }

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
}
