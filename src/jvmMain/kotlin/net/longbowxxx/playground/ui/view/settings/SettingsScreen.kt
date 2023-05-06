/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.settings

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val APP_CATEGORY_TEXT = "APP SETTINGS"
private const val CHAT_CATEGORY_TEXT = "CHAT SETTINGS"
private const val IMAGE_CATEGORY_TEXT = "IMAGE SETTINGS"

@Suppress("FunctionName")
@Composable
@Preview
fun SettingsScreen() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(APP_CATEGORY_TEXT, style = MaterialTheme.typography.headlineLarge)
        AppParamsView()
        Text(CHAT_CATEGORY_TEXT, style = MaterialTheme.typography.headlineLarge)
        ChatParamsView()
        Text(IMAGE_CATEGORY_TEXT, style = MaterialTheme.typography.headlineLarge)
        ImageParamsView()
    }
}
