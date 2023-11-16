/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val CLOSE_TEXT = "CLOSE"

@Suppress("FunctionName")
@Composable
fun DropdownCheckWidget(
    title: String,
    items: List<Pair<String, Boolean>>,
    onChanged: (Int, Boolean) -> Unit,
) {
    var showDropdown by remember { mutableStateOf(false) }

    Box(modifier = Modifier.width(200.dp)) {
        TextButton(
            onClick = { showDropdown = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("$title…")
        }
        DropdownMenu(
            showDropdown,
            onDismissRequest = {
                showDropdown = false
            },
            modifier = Modifier.width(400.dp),
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(item.second, {
                                onChanged(index, !item.second)
                            })
                            Text(item.first)
                        }
                    },
                    onClick = { onChanged(index, !item.second) },
                )
            }
            DropdownMenuItem(
                text = { Text(CLOSE_TEXT) },
                onClick = { showDropdown = false },
            )
        }
    }
}
