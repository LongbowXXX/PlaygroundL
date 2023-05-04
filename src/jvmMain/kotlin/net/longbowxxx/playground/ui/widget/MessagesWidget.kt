/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.ui.tertiaryButtonColors
import net.longbowxxx.playground.viewmodel.chatViewModel

private const val ADD_MESSAGE_TEXT = "ADD MESSAGE"
private const val CLEAR_MESSAGES_TEXT = "CLEAR MESSAGES"
private const val SUBMIT_TEXT = "SUBMIT"

@Suppress("FunctionName")
@Composable
fun ColumnScope.MessagesWidget() {
    val listState = rememberLazyListState()
    val messages by remember { chatViewModel.messages }
    val requesting by remember { chatViewModel.requesting }

    LazyColumn(
        modifier = Modifier.weight(1f).padding(10.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(10.dp)),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        state = listState,
    ) {
        itemsIndexed(messages) { index, message ->
            MessageItemWidget(index, message)
        }
    }
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            // リストに要素が追加された場合に末尾にスクロール
            listState.scrollToItem(index = messages.size - 1)
        }
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(10.dp),
    ) {
        Button(
            { chatViewModel.addMessage() },
            enabled = !requesting,
        ) {
            Text(ADD_MESSAGE_TEXT)
        }
        Button(
            { chatViewModel.requestChat() },
            enabled = !requesting,
        ) {
            Text(SUBMIT_TEXT)
        }
        Button(
            { chatViewModel.clearMessages() },
            colors = tertiaryButtonColors(),
            enabled = !requesting,
        ) {
            Text(CLEAR_MESSAGES_TEXT)
        }
    }
}
