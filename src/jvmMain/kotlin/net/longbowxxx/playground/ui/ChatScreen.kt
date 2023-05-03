/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui

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
import net.longbowxxx.playground.ui.widget.MessagesWidget
import net.longbowxxx.playground.ui.widget.SystemPromptWidget
import net.longbowxxx.playground.viewmodel.chatViewModel

@Suppress("FunctionName")
@Composable
fun ChatScreen() {
    var errorMessage by remember { chatViewModel.errorMessage }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.weight(0.5f).fillMaxHeight()) {
            SystemPromptWidget()
        }
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            MessagesWidget()
        }
    }
    ErrorWidget(errorMessage) { errorMessage = "" }
}
