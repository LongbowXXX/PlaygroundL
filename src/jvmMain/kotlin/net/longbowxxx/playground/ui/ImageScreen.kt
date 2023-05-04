/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.ui.widget.ErrorWidget
import net.longbowxxx.playground.ui.widget.ImageListWidget
import net.longbowxxx.playground.ui.widget.ImagePromptWidget
import net.longbowxxx.playground.viewmodel.imageViewModel

private const val PROMPT_LABEL_TEXT = "PROMPT IN ENGLISH"

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
@Preview
fun ImageScreen() {
    var errorMessage by remember { imageViewModel.errorMessage }

    Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        ImagePromptWidget()
        ImageListWidget()
    }
    ErrorWidget(errorMessage) { errorMessage = "" }
}
