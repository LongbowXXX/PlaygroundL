/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.discuss

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.longbowxxx.generativeai.DiscussMessage
import net.longbowxxx.playground.ui.widget.QuickLoadWidget
import net.longbowxxx.playground.viewmodel.appProperties
import net.longbowxxx.playground.viewmodel.discussViewModel
import java.awt.event.KeyEvent

private const val CONTENT_TEXT = "CONTENT IN ENGLISH"

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun DiscussMessageItemView(
    index: Int,
    message: DiscussMessage,
) {
    val requesting by remember { discussViewModel.requesting }
    val messageFontSizeSp by remember { appProperties.messageFontSizeSp }
    val chatMessageFileList = discussViewModel.chatMessageFileList

    ElevatedCard(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(5.dp, 5.dp, 5.dp, 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(message.author)

                QuickLoadWidget(chatMessageFileList) {
                    val newContent = it.readText(Charsets.UTF_8)
                    discussViewModel.updateMessage(index, DiscussMessage(message.author, newContent))
                }

                IconButton(
                    { discussViewModel.removeMessage(index) },
                    enabled = !requesting,
                ) {
                    Icon(Icons.Default.Clear, null)
                }
            }
            val content = message.content
            TextField(
                content,
                {
                    discussViewModel.updateMessage(index, DiscussMessage(message.author, it))
                },
                label = {
                    Text(CONTENT_TEXT)
                },
                modifier =
                    Modifier.fillMaxWidth()
                        .onKeyEvent {
                            // Alt + Enter を押すと、request
                            if (it.type == KeyEventType.KeyUp &&
                                it.key.nativeKeyCode == KeyEvent.VK_ENTER &&
                                it.isAltPressed
                            ) {
                                discussViewModel.requestChat()
                                true
                            } else {
                                false
                            }
                        },
                readOnly = requesting,
                textStyle = TextStyle(fontSize = messageFontSizeSp.sp),
            )
        }
    }
}
