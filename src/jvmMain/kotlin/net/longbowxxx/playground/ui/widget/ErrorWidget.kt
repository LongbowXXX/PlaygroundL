/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

private const val OK_BUTTON_TEXT = "OK"
private const val ERROR_TITLE_TEXT = "ERROR"

@OptIn(ExperimentalMaterialApi::class)
@Suppress("FunctionName")
@Composable
fun ErrorWidget(errorMessage: String, onClosed: () -> Unit) {
    if (errorMessage.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { onClosed() },
            confirmButton = {
                Button(
                    { onClosed() },
                ) {
                    Text(OK_BUTTON_TEXT)
                }
            },
            title = { Text(ERROR_TITLE_TEXT) },
            text = {
                Text(
                    errorMessage,
                    color = MaterialTheme.colorScheme.error,
                )
            },
        )
    }
}
