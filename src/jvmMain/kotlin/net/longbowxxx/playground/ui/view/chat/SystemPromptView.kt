/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.longbowxxx.playground.ui.widget.AudioTransWidget
import net.longbowxxx.playground.ui.widget.DropdownCheckWidget
import net.longbowxxx.playground.ui.widget.FileChooseWidget
import net.longbowxxx.playground.ui.widget.QuickLoadWidget
import net.longbowxxx.playground.viewmodel.appProperties
import net.longbowxxx.playground.viewmodel.chatProperties
import net.longbowxxx.playground.viewmodel.chatViewModel
import net.longbowxxx.playground.viewmodel.createAudioViewModel
import java.io.File

private const val SYSTEM_PROMPT_LABEL_TEXT = "SYSTEM"
private const val LOAD_SYSTEM_TEXT = "LOAD SYSTEM PROMPT…"
private val audioViewModel = createAudioViewModel()

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun ColumnScope.SystemPromptView() {
    var systemPrompt by remember { chatProperties.chatSystemPrompt }
    val requesting by remember { chatViewModel.requesting }
    val verticalScrollState = rememberScrollState(0)
    var systemMessage by remember { chatProperties.chatSystemPrompt }
    val messageFontSizeSp by remember { appProperties.messageFontSizeSp }
    val allFunctions by remember { chatViewModel.allFunctions }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        FileChooseWidget(LOAD_SYSTEM_TEXT) { selectedFile ->
            selectedFile?.let {
                systemMessage = File(it).readText(Charsets.UTF_8)
            }
        }
        QuickLoadWidget(chatViewModel.chatPromptFileList) {
            systemPrompt = it.readText(Charsets.UTF_8)
        }
        AudioTransWidget(audioViewModel, true) {
            systemPrompt = it
        }
    }

    val functionStrList = allFunctions.map { it.first.functionSpec.name to it.second }
    DropdownCheckWidget("FUNCTION", functionStrList) { index, enabled ->
        chatViewModel.updateFunctionEnabled(index, enabled)
    }

    TextField(
        systemPrompt,
        {
            systemPrompt = it
        },
        label = {
            Text(SYSTEM_PROMPT_LABEL_TEXT)
        },
        modifier = Modifier.fillMaxWidth()
            .verticalScroll(verticalScrollState)
            .weight(1f)
            .padding(10.dp),
        readOnly = requesting,
        textStyle = TextStyle(fontSize = messageFontSizeSp.sp),
    )
}
