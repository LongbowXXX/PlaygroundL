/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.image

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.ui.widget.ErrorWidget
import net.longbowxxx.playground.viewmodel.imageViewModel

@Suppress("FunctionName")
@Composable
@Preview
fun ImageScreen() {
    var errorMessage by remember { imageViewModel.errorMessage }

    Row(modifier = Modifier.padding(10.dp).fillMaxHeight(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        ImagePromptView()
        ImageView()
    }
    ErrorWidget(errorMessage) { errorMessage = "" }
}
