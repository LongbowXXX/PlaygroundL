/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

const val CHAT_TAB = 0
const val DISCUSS_TAB = 1
const val IMAGE_TAB = 2
const val SETTING_TAB = 3

private const val CHAT_TEXT = "CHAT"
private const val DISCUSS_TEXT = "DISCUSS"
private const val IMAGE_TEXT = "IMAGE"
private const val SETTINGS_TEXT = "SETTINGS"

private val bottomItems =
    listOf(
        CHAT_TEXT,
        DISCUSS_TEXT,
        IMAGE_TEXT,
        SETTINGS_TEXT,
    )

fun Int.toTabName() =
    when (this) {
        0 -> CHAT_TEXT
        1 -> DISCUSS_TEXT
        2 -> IMAGE_TEXT
        3 -> SETTINGS_TEXT
        else -> error("Unknown Tab Index $this")
    }

@Suppress("FunctionName")
@Composable
fun BottomBar(
    selectedTab: Int,
    onSelected: (Int) -> Unit,
) {
    val aiChatIcon = painterResource("ai-chat-icon.png")
    val iconModifier = Modifier.width(32.dp).height(32.dp)
    NavigationBar {
        bottomItems.forEachIndexed { index, item ->
            val selectedItem = selectedTab == index
            NavigationBarItem(
                icon = {
                    when (item) {
                        CHAT_TEXT ->
                            Icon(
                                painter = aiChatIcon,
                                contentDescription = item,
                                modifier = iconModifier,
                            )

                        DISCUSS_TEXT ->
                            Icon(
                                painter = aiChatIcon,
                                contentDescription = item,
                                modifier = iconModifier,
                            )

                        IMAGE_TEXT ->
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = item,
                                modifier = iconModifier,
                            )

                        SETTINGS_TEXT ->
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = item,
                                modifier = iconModifier,
                            )
                    }
                },
                selected = selectedItem,
                onClick = {
                    onSelected(index)
                },
                label = {
                    Text(item)
                },
            )
        }
    }
}
