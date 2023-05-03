/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.viewmodel.chatViewModel
import net.longbowxxx.playground.viewmodel.imageViewModel

private const val SUBMIT_TEXT = "SUBMIT"

@Suppress("FunctionName")
@Composable
fun SubmitActionButton(selectedTab: Int) {
    when (selectedTab) {
        CHAT_TAB -> ChatActionButton()
        IMAGE_TAB -> ImageActionButton()
    }
}

@Suppress("FunctionName")
@Composable
private fun ChatActionButton() {
    val requesting by remember { chatViewModel.requesting }
    if (!requesting) {
        FloatingActionButton(
            { chatViewModel.requestChat() },
            contentColor = MaterialTheme.colorScheme.onSecondary,
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.width(120.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(Icons.Default.Send, null)
                Text(SUBMIT_TEXT)
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
private fun ImageActionButton() {
    val requesting by remember { imageViewModel.requesting }
    if (!requesting) {
        FloatingActionButton(
            { imageViewModel.requestCreateImage() },
            contentColor = MaterialTheme.colorScheme.onSecondary,
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.width(120.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(Icons.Default.Send, null)
                Text(SUBMIT_TEXT)
            }
        }
    }
}
