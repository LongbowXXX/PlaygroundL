/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.discuss

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.ui.widget.DiscussHistorySelectorWidget
import net.longbowxxx.playground.viewmodel.DiscussViewModel
import net.longbowxxx.playground.viewmodel.discussViewModel

private const val NEW_SESSION_TEXT = "NEW SESSION"
private const val SUBMIT_TEXT = "SUBMIT"
private const val DISCUSS_HISTORY_TEXT = "RESTORE OLD DISCUSS"

@Suppress("FunctionName")
@Composable
fun ColumnScope.DiscussMessagesView() {
    val listState = rememberLazyListState()
    val messages by remember { discussViewModel.messages }
    val requesting by remember { discussViewModel.requesting }
    val lastMessageSize = messages.lastOrNull()?.content?.length ?: 0

    LazyColumn(
        modifier =
            Modifier.weight(1f).padding(10.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(10.dp)),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        state = listState,
    ) {
        itemsIndexed(messages) { index, message ->
            DiscussMessageItemView(index, message)
        }
        item {
            // メッセージ追加用ボタン
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    { discussViewModel.addMessage(DiscussViewModel.USER_AUTHOR) },
                    enabled = !requesting,
                ) {
                    Icon(Icons.Default.Add, null)
                }
            }
        }
    }

    LaunchedEffect(messages.size) {
        // リストに要素が追加された場合に末尾にスクロール
        // 末尾の追加ボタンを考慮したIndexを指定
        listState.scrollToItem(index = messages.size)
    }

    if (requesting) {
        LaunchedEffect(lastMessageSize) {
            // 最後のメッセージが更新された際に、末尾にスクロール
            // 末尾の追加ボタンを考慮したIndexを指定
            listState.scrollToItem(index = messages.size)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(10.dp),
    ) {
        Button(
            { discussViewModel.requestChat() },
            enabled = !requesting,
        ) {
            Text(SUBMIT_TEXT)
        }
        DiscussHistorySelectorWidget(DISCUSS_HISTORY_TEXT) {
            discussViewModel.restoreOldSession(it)
        }
        TextButton(
            { discussViewModel.newSession() },
            enabled = !requesting,
        ) {
            Icon(Icons.Default.Add, null)
            Text(NEW_SESSION_TEXT)
        }
    }
}
