/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

private const val SAVE_BUTTON_TEXT = "SAVE"

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun SecretTextInputWidget(
    label: String,
    readOnly: Boolean,
    onSaved: (String) -> Unit,
) {
    var secretText by remember { mutableStateOf("") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(10.dp),
    ) {
        TextField(
            secretText,
            { secretText = it },
            readOnly = readOnly,
            label = { Text(label) },
            modifier = Modifier.weight(1f),
            visualTransformation = PasswordVisualTransformation(),
        )
        Button(
            {
                onSaved(secretText)
                secretText = ""
            },
        ) {
            Text(SAVE_BUTTON_TEXT)
        }
    }
}
