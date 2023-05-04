/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.preference

import androidx.compose.runtime.mutableStateOf
import java.io.File
import java.util.*

class ImagePreference : PreferenceBase() {
    companion object {
        private const val IMAGE_CREATE_NUMBER_KEY = "image.create.number"
        private const val IMAGE_EDIT_NUMBER_KEY = "image.edit.number"
        private const val IMAGE_VARIATION_NUMBER_KEY = "image.variation.number"
        private const val DEFAULT_IMAGE_NUMBER = 1

        private const val FILE_NAME = "image.properties"
        private const val FILE_COMMENT = "Playground Image Properties"
    }

    private val properties = Properties()
    val numberOfCreate = mutableStateOf(DEFAULT_IMAGE_NUMBER)
    val numberOfEdit = mutableStateOf(DEFAULT_IMAGE_NUMBER)
    val numberOfVariation = mutableStateOf(DEFAULT_IMAGE_NUMBER)

    fun load() {
        runCatching {
            File(FILE_NAME).reader(Charsets.UTF_8).use { reader ->
                properties.load(reader)
            }
            numberOfCreate.value = properties.getIntProperty(IMAGE_CREATE_NUMBER_KEY, DEFAULT_IMAGE_NUMBER)
            numberOfEdit.value = properties.getIntProperty(IMAGE_EDIT_NUMBER_KEY, DEFAULT_IMAGE_NUMBER)
            numberOfVariation.value = properties.getIntProperty(IMAGE_VARIATION_NUMBER_KEY, DEFAULT_IMAGE_NUMBER)
        }.onFailure {
            save()
        }
    }

    private fun save() {
        properties.setProperty(IMAGE_CREATE_NUMBER_KEY, numberOfCreate.value.toString())
        properties.setProperty(IMAGE_EDIT_NUMBER_KEY, numberOfEdit.value.toString())
        properties.setProperty(IMAGE_VARIATION_NUMBER_KEY, numberOfVariation.value.toString())
        File(FILE_NAME).writer(Charsets.UTF_8).use { writer ->
            properties.store(writer, FILE_COMMENT)
        }
    }

    override fun close() {
        save()
    }
}
