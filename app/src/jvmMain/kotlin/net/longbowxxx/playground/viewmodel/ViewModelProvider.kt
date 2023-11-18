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

/**
 * Application properties.
 */
val appProperties: PlaygroundPreference by lazy {
    PlaygroundPreference(appDataDirectory).apply {
        load()
    }
}

/**
 * Chat properties.
 */
val chatProperties: ChatPreference by lazy {
    ChatPreference(appDataDirectory).apply {
        load()
    }
}

/**
 * Discuss properties.
 */
val discussProperties: DiscussPreference by lazy {
    DiscussPreference(appDataDirectory).apply {
        load()
    }
}

/**
 * Image properties.
 */
val imageProperties: ImagePreference by lazy {
    ImagePreference(appDataDirectory).apply {
        load()
    }
}

/**
 * Chat history.
 */
val chatHistory: ChatHistory by lazy {
    ChatHistory(appDataDirectory)
}

/**
 * Discuss history.
 */
val discussHistory: DiscussHistory by lazy {
    DiscussHistory(appDataDirectory)
}

/**
 * Chat ViewModel.
 */
val chatViewModel: ChatViewModel by lazy {
    ChatViewModel()
}

/**
 * Discuss ViewModel.
 */
val discussViewModel: DiscussViewModel by lazy {
    DiscussViewModel()
}

/**
 * Image ViewModel.
 */
val imageViewModel: ImageViewModel by lazy {
    ImageViewModel()
}

/**
 * Create a new SpeechRecognitionViewModel.
 */
fun createAudioViewModel() = SpeechRecognitionViewModel().also { speechRecognitionViewModels.add(it) }

private val speechRecognitionViewModels = mutableListOf<SpeechRecognitionViewModel>()

/**
 * Close all ViewModels and properties.
 */
fun closeViewModelAndProperties() {
    chatViewModel.close()
    imageViewModel.close()
    speechRecognitionViewModels.forEach { it.close() }
    chatProperties.close()
    imageProperties.close()
    appProperties.close()
    chatHistory.close()
}
