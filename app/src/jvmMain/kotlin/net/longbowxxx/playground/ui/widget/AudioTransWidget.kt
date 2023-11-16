/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import net.longbowxxx.playground.viewmodel.SpeechRecognitionViewModel

@Suppress("FunctionName")
@Composable
fun AudioTransWidget(
    speechRecognitionViewModel: SpeechRecognitionViewModel,
    isTranscription: Boolean,
    onResult: (String) -> Unit,
) {
    var transText by remember { speechRecognitionViewModel.transText }
    val micImage = painterResource("mic-icon.png")
    val audioState by remember { speechRecognitionViewModel.state }
    val micAvailable = speechRecognitionViewModel.isMicAvailable
    TextButton(
        {
            when (audioState) {
                SpeechRecognitionViewModel.State.STOPPED -> speechRecognitionViewModel.startTrans(isTranscription)
                SpeechRecognitionViewModel.State.RECORDING -> speechRecognitionViewModel.stopRecording()
                else -> {
                    // nothing to do
                }
            }
        },
        enabled = audioState != SpeechRecognitionViewModel.State.REQUESTING && micAvailable,
    ) {
        val color =
            when (audioState) {
                SpeechRecognitionViewModel.State.STOPPED -> MaterialTheme.colorScheme.primary
                SpeechRecognitionViewModel.State.RECORDING -> MaterialTheme.colorScheme.error
                SpeechRecognitionViewModel.State.REQUESTING -> MaterialTheme.colorScheme.primary
            }

        Icon(micImage, null, tint = color)
    }
    if (transText.isNotEmpty()) {
        onResult(transText)
        transText = ""
    }
}
