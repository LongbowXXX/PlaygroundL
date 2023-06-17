/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.discuss

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
import net.longbowxxx.playground.ui.widget.FileChooseWidget
import net.longbowxxx.playground.ui.widget.QuickLoadWidget
import net.longbowxxx.playground.viewmodel.appProperties
import net.longbowxxx.playground.viewmodel.createAudioViewModel
import net.longbowxxx.playground.viewmodel.discussProperties
import net.longbowxxx.playground.viewmodel.discussViewModel
import java.io.File

private const val CONTEXT_LABEL_TEXT = "CONTEXT IN ENGLISH"
private const val LOAD_CONTEXT_TEXT = "LOAD CONTEXT…"
private val audioViewModel = createAudioViewModel()

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun ColumnScope.DiscussContextView() {
    var discussContext by remember { discussProperties.discussContext }
    val requesting by remember { discussViewModel.requesting }
    val verticalScrollState = rememberScrollState(0)
    val messageFontSizeSp by remember { appProperties.messageFontSizeSp }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        FileChooseWidget(LOAD_CONTEXT_TEXT) { selectedFile ->
            selectedFile?.let {
                discussContext = File(it).readText(Charsets.UTF_8)
            }
        }
        QuickLoadWidget(discussViewModel.chatPromptFileList) {
            discussContext = it.readText(Charsets.UTF_8)
        }
        AudioTransWidget(audioViewModel, true) {
            discussContext = it
        }
    }

    TextField(
        discussContext,
        {
            discussContext = it
        },
        label = {
            Text(CONTEXT_LABEL_TEXT)
        },
        modifier = Modifier.fillMaxWidth()
            .verticalScroll(verticalScrollState)
            .weight(1f)
            .padding(10.dp),
        readOnly = requesting,
        textStyle = TextStyle(fontSize = messageFontSizeSp.sp),
    )
}
