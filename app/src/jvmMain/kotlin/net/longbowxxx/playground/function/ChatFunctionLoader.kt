/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.function

import net.longbowxxx.openai.client.OpenAiSettings
import net.longbowxxx.openai.client.function.ChatFunctionPlugin
import net.longbowxxx.openai.client.function.CreateImageFunctionPlugin
import net.longbowxxx.search.GoogleCustomSearchClientSettings
import net.longbowxxx.search.function.WebSearchPlugin
import net.longbowxxx.utils.DebugLoggable
import net.longbowxxx.utils.logInfo
import java.io.File

/**
 * Chat function loader.
 */
class ChatFunctionLoader(
    appDataDir: File,
    openAiSettings: OpenAiSettings,
    googleSearchSettings: GoogleCustomSearchClientSettings,
) : DebugLoggable {
    private val nativePlugins =
        listOf(
            SaveStringToFileFunctionPlugin(),
            CreateImageFunctionPlugin(appDataDir, openAiSettings),
            ShowImagePlugin(),
            WebSearchPlugin(googleSearchSettings),
            ReadDataPlugin(),
            ReadWebPlugin(),
        )

    /**
     * Load plugins from directory.
     *
     * @param directory Plugin directory.
     */
    fun loadPlugins(directory: File): List<ChatFunctionPlugin> {
        val allPlugins =
            directory.walkTopDown().filter {
                it.isDirectory
            }.mapNotNull { pluginDirectory ->
                loadPlugin(pluginDirectory)
            }.toMutableList()
        allPlugins.addAll(nativePlugins)
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
