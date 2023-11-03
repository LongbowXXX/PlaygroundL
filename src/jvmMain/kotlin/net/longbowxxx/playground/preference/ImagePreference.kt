/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.preference

import androidx.compose.runtime.mutableStateOf

class ImagePreference(appName: String) : PreferenceBase(appName) {
    companion object {
        private const val IMAGE_CREATE_NUMBER_KEY = "image.create.number"
        private const val IMAGE_EDIT_NUMBER_KEY = "image.edit.number"
        private const val IMAGE_VARIATION_NUMBER_KEY = "image.variation.number"
        private const val IMAGE_TRANSLATION_PROMPT_KEY = "image.translation.prompt"
        private const val DEFAULT_IMAGE_NUMBER = 1
        private const val DEFAULT_TRANSLATION_PROMPT = "英語に翻訳してください"

        private const val FILE_NAME = "image.properties"
        private const val FILE_COMMENT = "Playground Image Properties"
    }

    override val fileName = FILE_NAME
    override val fileComment = FILE_COMMENT

    val numberOfCreate = mutableStateOf(DEFAULT_IMAGE_NUMBER)
    val numberOfEdit = mutableStateOf(DEFAULT_IMAGE_NUMBER)
    val numberOfVariation = mutableStateOf(DEFAULT_IMAGE_NUMBER)
    val translationPrompt = mutableStateOf(DEFAULT_TRANSLATION_PROMPT)

    override fun load() {
        loadInternal {
            numberOfCreate.value = getIntProperty(IMAGE_CREATE_NUMBER_KEY, DEFAULT_IMAGE_NUMBER)
            numberOfEdit.value = getIntProperty(IMAGE_EDIT_NUMBER_KEY, DEFAULT_IMAGE_NUMBER)
            numberOfVariation.value = getIntProperty(IMAGE_VARIATION_NUMBER_KEY, DEFAULT_IMAGE_NUMBER)
            translationPrompt.value = getProperty(IMAGE_TRANSLATION_PROMPT_KEY, DEFAULT_TRANSLATION_PROMPT)
        }
    }

    override fun save() {
        saveInternal {
            setProperty(IMAGE_CREATE_NUMBER_KEY, numberOfCreate.value.toString())
            setProperty(IMAGE_EDIT_NUMBER_KEY, numberOfEdit.value.toString())
            setProperty(IMAGE_VARIATION_NUMBER_KEY, numberOfVariation.value.toString())
            setProperty(IMAGE_TRANSLATION_PROMPT_KEY, translationPrompt.value)
        }
    }
}
