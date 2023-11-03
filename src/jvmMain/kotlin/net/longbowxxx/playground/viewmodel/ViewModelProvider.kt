/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.viewmodel

import net.longbowxxx.playground.history.ChatHistory
import net.longbowxxx.playground.history.DiscussHistory
import net.longbowxxx.playground.preference.ChatPreference
import net.longbowxxx.playground.preference.DiscussPreference
import net.longbowxxx.playground.preference.ImagePreference
import net.longbowxxx.playground.preference.PlaygroundPreference
import net.longbowxxx.playground.utils.appDataDirectory

val appProperties: PlaygroundPreference by lazy {
    PlaygroundPreference(appDataDirectory).apply {
        load()
    }
}

val chatProperties: ChatPreference by lazy {
    ChatPreference(appDataDirectory).apply {
        load()
    }
}

val discussProperties: DiscussPreference by lazy {
    DiscussPreference(appDataDirectory).apply {
        load()
    }
}

val imageProperties: ImagePreference by lazy {
    ImagePreference(appDataDirectory).apply {
        load()
    }
}

val chatHistory: ChatHistory by lazy {
    ChatHistory(appDataDirectory)
}

val discussHistory: DiscussHistory by lazy {
    DiscussHistory(appDataDirectory)
}

val chatViewModel: ChatViewModel by lazy {
    ChatViewModel()
}

val discussViewModel: DiscussViewModel by lazy {
    DiscussViewModel()
}

val imageViewModel: ImageViewModel by lazy {
    ImageViewModel()
}

fun createAudioViewModel() = AudioViewModel().also { audioViewModels.add(it) }

private val audioViewModels = mutableListOf<AudioViewModel>()

fun closeViewModelAndProperties() {
    chatViewModel.close()
    imageViewModel.close()
    audioViewModels.forEach { it.close() }
    chatProperties.close()
    imageProperties.close()
    appProperties.close()
    chatHistory.close()
}
