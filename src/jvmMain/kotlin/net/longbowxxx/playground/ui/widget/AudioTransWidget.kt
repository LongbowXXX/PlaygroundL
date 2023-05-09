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
import androidx.compose.ui.res.painterResource
import net.longbowxxx.playground.viewmodel.AudioViewModel
import net.longbowxxx.playground.viewmodel.audioViewModel

@Suppress("FunctionName")
@Composable
fun AudioTransWidget(onResult: (String) -> Unit) {
    val transText by remember { audioViewModel.transText }
    val micImage = painterResource("mic-icon.png")
    val audioState by remember { audioViewModel.state }
    TextButton(
        {
            when (audioState) {
                AudioViewModel.State.STOPPED -> audioViewModel.startTranscription()
                AudioViewModel.State.RECORDING -> audioViewModel.stopRecording()
                else -> {
                    // nothing to do
                }
            }
        },
        enabled = audioState != AudioViewModel.State.REQUESTING,
    ) {
        val color = when (audioState) {
            AudioViewModel.State.STOPPED -> MaterialTheme.colorScheme.primary
            AudioViewModel.State.RECORDING -> MaterialTheme.colorScheme.error
            AudioViewModel.State.REQUESTING -> MaterialTheme.colorScheme.primary
        }

        Icon(micImage, null, tint = color)
    }
    onResult(transText)
}
