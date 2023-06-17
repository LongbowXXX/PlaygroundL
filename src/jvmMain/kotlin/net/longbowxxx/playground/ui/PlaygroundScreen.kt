/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import net.longbowxxx.playground.ui.view.chat.ChatScreen
import net.longbowxxx.playground.ui.view.discuss.DiscussScreen
import net.longbowxxx.playground.ui.view.image.ImageScreen
import net.longbowxxx.playground.ui.view.settings.SettingsScreen
import net.longbowxxx.playground.ui.widget.BottomBar
import net.longbowxxx.playground.ui.widget.CHAT_TAB
import net.longbowxxx.playground.ui.widget.DISCUSS_TAB
import net.longbowxxx.playground.ui.widget.IMAGE_TAB
import net.longbowxxx.playground.ui.widget.SETTING_TAB
import net.longbowxxx.playground.ui.widget.toTabName
import net.longbowxxx.playground.utils.DebugLogLevel
import net.longbowxxx.playground.utils.log
import net.longbowxxx.playground.viewmodel.appProperties
import net.longbowxxx.playground.viewmodel.closeViewModelAndProperties
import kotlin.math.roundToInt

private const val TITLE_TEXT = "PLAYGROUND"

@Suppress("FunctionName")
@Composable
@Preview
fun ApplicationScope.PlaygroundWindow() {
    var selectedTabName by remember { mutableStateOf("") }
    val windowState = rememberWindowState(
        position = WindowPosition(appProperties.windowLeft.dp, appProperties.windowTop.dp),
        size = DpSize(appProperties.windowWidth.dp, appProperties.windowHeight.dp),
    )

    Window(
        onCloseRequest = {
            log(DebugLogLevel.TRACE, "PlaygroundWindow") { "onCloseRequest" }
            appProperties.updateWindowState(
                windowState.position.x.value.roundToInt(),
                windowState.position.y.value.roundToInt(),
                windowState.size.width.value.roundToInt(),
                windowState.size.height.value.roundToInt(),
            )
            closeViewModelAndProperties()
            exitApplication()
        },
        title = "$TITLE_TEXT - $selectedTabName",
        state = windowState,
        icon = appLogoMini,
    ) {
        PlaygroundScreen {
            selectedTabName = it
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun PlaygroundScreen(onTabSelected: (String) -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                BottomBar(selectedTab) { selectedTab = it }
            },
        ) {
            // padding は bottomBar を考慮
            Column(
                modifier = Modifier.padding(
                    10.dp,
                    10.dp,
                    10.dp,
                    76.dp,
                ).fillMaxSize(),
            ) {
                onTabSelected(selectedTab.toTabName())
                when (selectedTab) {
                    CHAT_TAB -> ChatScreen()
                    DISCUSS_TAB -> DiscussScreen()
                    IMAGE_TAB -> ImageScreen()
                    SETTING_TAB -> SettingsScreen()
                }
            }
        }
    }
}
