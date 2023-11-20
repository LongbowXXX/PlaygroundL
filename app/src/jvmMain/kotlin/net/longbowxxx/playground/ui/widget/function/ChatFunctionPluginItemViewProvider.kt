/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget.function

import net.longbowxxx.openai.client.function.ChatFunctionPlugin
import net.longbowxxx.playground.function.ShowImagePlugin
import net.longbowxxx.playground.viewmodel.chatViewModel

val chatFunctionPluginItemViews: List<ChatFunctionPluginItemView> by lazy {
    listOf(
        ShowImagePluginItemView(findPlugin<ShowImagePlugin>()),
    )
}

private inline fun <reified T : ChatFunctionPlugin> findPlugin(): T {
    return chatViewModel.allFunctions.value.first {
        it.first is T
    }.first as T
}
