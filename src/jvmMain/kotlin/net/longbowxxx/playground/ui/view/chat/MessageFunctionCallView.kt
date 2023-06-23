/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.chat

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import net.longbowxxx.openai.client.OpenAiChatFunctionCallMessage
import net.longbowxxx.playground.viewmodel.appProperties

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun MessageFunctionCallView(functionCall: OpenAiChatFunctionCallMessage) {
    val content = """
## FunctionCall
### name:
${functionCall.name}
### arguments:
${functionCall.arguments}
    """.trimIndent()

    val messageFontSizeSp by remember { appProperties.messageFontSizeSp }
    TextField(
        content,
        {
            // nothing to do.
        },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        textStyle = TextStyle(fontSize = messageFontSizeSp.sp),
    )
}
