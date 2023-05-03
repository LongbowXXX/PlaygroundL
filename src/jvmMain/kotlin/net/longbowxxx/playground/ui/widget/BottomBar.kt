/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

const val CHAT_TAB = 0
const val IMAGE_TAB = 1
const val SETTING_TAB = 2

private const val CHAT_TEXT = "CHAT"
private const val IMAGE_TEXT = "IMAGE"
private const val SETTINGS_TEXT = "SETTINGS"
private val bottomItems = listOf(
    CHAT_TEXT to Icons.Default.Face,
    IMAGE_TEXT to Icons.Filled.Edit,
    SETTINGS_TEXT to Icons.Default.Settings,
)

@Suppress("FunctionName")
@Composable
fun BottomBar(selectedTab: Int, onSelected: (Int) -> Unit) {
    BottomAppBar {
        bottomItems.forEachIndexed { index, pair ->
            BottomNavigationItem(
                icon = {
                    Icon(imageVector = pair.second, contentDescription = pair.first)
                },
                selected = selectedTab == index,
                onClick = {
                    onSelected(index)
                },
                label = { Text(pair.first) },
            )
        }
    }
}
