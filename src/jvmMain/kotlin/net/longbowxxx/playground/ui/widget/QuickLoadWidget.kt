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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File

private const val QUICK_LOAD_TEXT = "QUICK LOAD…"

@Suppress("FunctionName")
@Composable
fun QuickLoadWidget(
    fileList: List<File>,
    onSelected: (file: File) -> Unit,
) {
    var showModelDropdown by remember { mutableStateOf(false) }

    Box(modifier = Modifier.width(140.dp)) {
        TextButton(
            { showModelDropdown = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(QUICK_LOAD_TEXT)
        }

        DropdownMenu(
            showModelDropdown,
            {
                showModelDropdown = false
            },
            modifier = Modifier.width(200.dp),
        ) {
            fileList.forEach { file ->
                DropdownMenuItem(
                    { Text(file.name) },
                    onClick = {
                        onSelected(file)
                        showModelDropdown = false
                    },
                )
            }
        }
    }
}
