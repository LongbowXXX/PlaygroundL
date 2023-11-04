/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.settings

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.longbowxxx.playground.ui.appLogo

private const val APP_CATEGORY_TEXT = "APP SETTINGS"
private const val CHAT_CATEGORY_TEXT = "CHAT SETTINGS"
private const val IMAGE_CATEGORY_TEXT = "IMAGE SETTINGS"
private const val APPLICATION_LOGO_TEXT = "APPLICATION LOGO"
private const val LOGO_DESCRIPTION_TEXT = "Image created by GPT-4 and DALL-E3"

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
        Text(APPLICATION_LOGO_TEXT, style = MaterialTheme.typography.headlineLarge)
        Text(LOGO_DESCRIPTION_TEXT)
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Image(appLogo, null)
        }
    }
}
