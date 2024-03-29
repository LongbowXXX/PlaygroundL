/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.history.ChatHistory
import net.longbowxxx.playground.viewmodel.chatViewModel

@Suppress("FunctionName")
@Composable
fun ChatHistorySelectorWidget(
    buttonText: String,
    onSelected: (ChatHistory.ChatHistorySession) -> Unit,
) {
    var showModelDropdown by remember { mutableStateOf(false) }
    val chatHistory by remember { chatViewModel.history }
    val requesting by remember { chatViewModel.requesting }

    Box(modifier = Modifier.width(200.dp)) {
        TextButton(
            {
                showModelDropdown = true
                chatViewModel.updateHistory()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !requesting,
        ) {
            Text("$buttonText…")
        }
        DropdownMenu(
            showModelDropdown,
            {
                showModelDropdown = false
            },
            modifier = Modifier.width(800.dp),
        ) {
            chatHistory.forEach { session ->
                DropdownMenuItem(
                    {
                        val categories = session.categories.joinToString(", ")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text("${session.title} [$categories]")
                            IconButton(
                                { chatViewModel.removeHistory(session) },
                            ) {
                                Icon(Icons.Default.Delete, null)
                            }
                        }
                    },
                    onClick = {
                        onSelected(session)
                        showModelDropdown = false
                    },
                )
            }
        }
    }
}
