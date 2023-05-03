/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.viewmodel.chatProperties
import net.longbowxxx.playground.viewmodel.chatViewModel

@Suppress("FunctionName")
@Composable
fun ModelSelectorWidget() {
    var selectedModel by remember { chatProperties.chatModel }
    var showModelDropdown by remember { mutableStateOf(false) }

    Box(modifier = Modifier.width(200.dp)) {
        Button(
            { showModelDropdown = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("$selectedModel…")
        }
        DropdownMenu(
            showModelDropdown,
            {
                showModelDropdown = false
            },
            modifier = Modifier.width(200.dp),
        ) {
            chatViewModel.models.forEach { model ->
                DropdownMenuItem(
                    { Text(model) },
                    onClick = {
                        selectedModel = model
                        showModelDropdown = false
                    },
                )
            }
        }
    }
}
