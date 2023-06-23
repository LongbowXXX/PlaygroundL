/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.chat

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRoleTypes
import net.longbowxxx.playground.viewmodel.appProperties
import net.longbowxxx.playground.viewmodel.chatViewModel
import java.awt.event.KeyEvent

private const val CONTENT_TEXT = "CONTENT"

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun MessageContentView(index: Int, message: OpenAiChatMessage) {
    val requesting by remember { chatViewModel.requesting }
    val messageFontSizeSp by remember { appProperties.messageFontSizeSp }

    TextField(
        message.content.orEmpty(),
        {
            if (message.functionCall == null) {
                chatViewModel.updateMessageContent(index, it)
            }
        },
        label = {
            Text(CONTENT_TEXT)
        },
        modifier = Modifier.fillMaxWidth()
            .onKeyEvent {
                // Alt + Enter を押すと、request
                if (it.type == KeyEventType.KeyUp &&
                    it.key.nativeKeyCode == KeyEvent.VK_ENTER &&
                    it.isAltPressed
                ) {
                    chatViewModel.requestChat()
                    true
                } else {
                    false
                }
            },
        readOnly = requesting,
        textStyle = TextStyle(fontSize = messageFontSizeSp.sp),
    )

    val content = message.content
    if (message.role == OpenAiChatRoleTypes.FUNCTION && !content.isNullOrEmpty()) {
        chatViewModel.allFunctions.firstOrNull {
            it.functionSpec.name == message.name
        }?.FunctionView(content)
    }
}
