/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.function

import net.longbowxxx.playground.utils.DebugLoggable
import net.longbowxxx.playground.utils.logInfo
import java.io.File

class ChatFunctionLoader : DebugLoggable {
    private val nativePlugins =
        listOf(
            SaveStringToFileFunctionPlugin(),
            CreateImageFunctionPlugin(),
            ShowImagePlugin(),
            WebSearchPlugin(),
            ReadDataPlugin(),
            ReadWebPlugin(),
        )

    fun loadPlugins(directory: File): List<ChatFunctionPlugin> {
        val allPlugins =
            directory.walkTopDown().filter {
                it.isDirectory
            }.mapNotNull { pluginDirectory ->
                loadPlugin(pluginDirectory)
            }.toMutableList()
        allPlugins.addAll(
            nativePlugins,
        )
        return allPlugins
    }

    private fun loadPlugin(pluginDirectory: File): ChatFunctionPlugin? {
        if (MockFunctionPlugin.isMockPlugin(pluginDirectory)) {
            return MockFunctionPlugin.loadMockPlugin(pluginDirectory)
        }
        logInfo { "loadPlugin failed. ${pluginDirectory.absolutePath}" }
        return null
    }
}
