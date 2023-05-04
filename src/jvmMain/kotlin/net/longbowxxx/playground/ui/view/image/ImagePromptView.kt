/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.longbowxxx.playground.viewmodel.appProperties
import net.longbowxxx.playground.viewmodel.imageViewModel

private const val PROMPT_LABEL_TEXT = "PROMPT IN ENGLISH"
private const val PROMPT_WILL_BE_TRANSLATED_LABEL_TEXT = "PROMPT WILL BE TRANSLATED"
private const val TRANSLATE_TO_ENGLISH_LABEL_TEXT = "TRANSLATE TO ENGLISH"
private const val CREATE_IMAGE_LABEL_TEXT = "CREATE IMAGE"

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun RowScope.ImagePromptView() {
    var prompt by remember { imageViewModel.prompt }
    val verticalScrollState = rememberScrollState(0)
    val requesting by remember { imageViewModel.requesting }
    var promptJa by remember { imageViewModel.promptJa }
    val verticalScrollStateJa = rememberScrollState(0)
    val requestingTranslation by remember { imageViewModel.requestingTranslation }
    val messageFontSizeSp by remember { appProperties.messageFontSizeSp }

    Column(
        modifier = Modifier.weight(0.5f).fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        TextField(
            promptJa,
            {
                promptJa = it
            },
            label = {
                Text(PROMPT_WILL_BE_TRANSLATED_LABEL_TEXT)
            },
            modifier = Modifier.verticalScroll(verticalScrollStateJa).weight(1f).fillMaxWidth(),
            readOnly = requestingTranslation,
            textStyle = TextStyle(fontSize = messageFontSizeSp.sp),
        )
        LaunchedEffect(promptJa.length) {
            verticalScrollStateJa.scrollTo(verticalScrollStateJa.maxValue)
        }
        Button(
            { imageViewModel.requestTranslation() },
        ) {
            Text(TRANSLATE_TO_ENGLISH_LABEL_TEXT)
        }

        TextField(
            prompt,
            {
                prompt = it
            },
            label = {
                Text(PROMPT_LABEL_TEXT)
            },
            modifier = Modifier.verticalScroll(verticalScrollState).weight(1f).fillMaxWidth(),
            readOnly = requesting,
            textStyle = TextStyle(fontSize = messageFontSizeSp.sp),
        )
        LaunchedEffect(prompt.length) {
            verticalScrollState.scrollTo(verticalScrollState.maxValue)
        }
        Button(
            { imageViewModel.requestCreateImage() },
            enabled = !requesting,
        ) {
            Text(CREATE_IMAGE_LABEL_TEXT)
        }
    }
}
