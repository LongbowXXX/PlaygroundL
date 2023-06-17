/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.discuss

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import net.longbowxxx.playground.ui.widget.ErrorWidget
import net.longbowxxx.playground.viewmodel.discussViewModel

@Suppress("FunctionName")
@Composable
fun DiscussScreen() {
    var errorMessage by remember { discussViewModel.errorMessage }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(0.5f).fillMaxHeight()) {
            DiscussContextView()
        }
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            DiscussMessagesView()
        }
    }
    ErrorWidget(errorMessage) { errorMessage = "" }
}
