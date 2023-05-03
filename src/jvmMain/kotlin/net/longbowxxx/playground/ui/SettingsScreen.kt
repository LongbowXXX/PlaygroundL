/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.ui.widget.AppParamsWidget
import net.longbowxxx.playground.ui.widget.ChatParamsWidget

private const val APP_CATEGORY_TEXT = "APP SETTINGS"
private const val CHAT_CATEGORY_TEXT = "CHAT SETTINGS"

@Suppress("FunctionName")
@Composable
@Preview
fun SettingsScreen() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Text(APP_CATEGORY_TEXT, style = MaterialTheme.typography.headlineLarge)
        AppParamsWidget()
        Text(CHAT_CATEGORY_TEXT, style = MaterialTheme.typography.headlineLarge)
        ChatParamsWidget()
    }
}
