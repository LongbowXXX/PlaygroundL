/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRoleTypes
import net.longbowxxx.playground.ui.widget.QuickLoadWidget
import net.longbowxxx.playground.viewmodel.chatViewModel

@Suppress("FunctionName")
@Composable
fun MessageItemView(index: Int, message: OpenAiChatMessage) {
    val requesting by remember { chatViewModel.requesting }
    val chatMessageFileList = chatViewModel.chatMessageFileList

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp, 5.dp, 5.dp, 5.dp),
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(message.role.toDisplayText(), modifier = Modifier.width(150.dp).padding(horizontal = 10.dp))

                Text(
                    message.name.orEmpty(),
                    modifier = Modifier.width(300.dp),
                )

                QuickLoadWidget(chatMessageFileList) {
                    val newContent = it.readText(Charsets.UTF_8)
                    chatViewModel.updateMessageContent(index, newContent)
                }

                IconButton(
                    { chatViewModel.removeMessage(index) },
                    enabled = !requesting,
                ) {
                    Icon(Icons.Default.Clear, null)
                }
            }
            val content = when (message.role) {
                OpenAiChatRoleTypes.USER -> message.content.orEmpty()
                OpenAiChatRoleTypes.ASSISTANT -> message.content
                OpenAiChatRoleTypes.FUNCTION -> message.content.orEmpty()
                else -> "MessageItemView not supported type ${message.role}"
            }
            if (content != null) {
                MessageContentView(index, message)
            }
            val functionCall = message.functionCall
            if (functionCall != null) {
                MessageFunctionCallView(functionCall)
            }
        }
    }
}

fun OpenAiChatRoleTypes.toDisplayText(): String {
    return when (this) {
        OpenAiChatRoleTypes.USER -> "USER"
        OpenAiChatRoleTypes.ASSISTANT -> "ASSISTANT"
        OpenAiChatRoleTypes.FUNCTION -> "FUNCTION"
        OpenAiChatRoleTypes.SYSTEM -> error("Invalid type SYSTEM selected.")
    }
}
