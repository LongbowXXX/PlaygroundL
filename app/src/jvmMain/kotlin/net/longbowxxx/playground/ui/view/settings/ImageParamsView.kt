/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import net.longbowxxx.playground.ui.widget.TextSlider
import net.longbowxxx.playground.viewmodel.imageProperties
import kotlin.math.roundToInt

private const val NUM_OF_CREATE_TEXT = "NUMBER OF CREATE"
private const val NUM_OF_EDIT_TEXT = "NUMBER OF EDIT"
private const val NUM_OF_VARIATION_TEXT = "NUMBER OF VARIATION"
private const val TRANSLATE_TO_ENGLISH_PROMPT_TEXT = "PROMPT TO TRANSLATE TO ENGLISH"

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun ImageParamsView() {
    var numberOfCreate by remember { imageProperties.numberOfCreate }
    var numberOfEdit by remember { imageProperties.numberOfEdit }
    var numberOfVariation by remember { imageProperties.numberOfVariation }
    var translationPrompt by remember { imageProperties.translationPrompt }

    // DALL-E 3 は、現時点では同時に一つしか画像を生成できない
    TextSlider(
        "$NUM_OF_CREATE_TEXT : $numberOfCreate",
        numberOfCreate.toFloat(),
        1f..1f,
    ) {
        numberOfCreate = it.roundToInt()
    }

    TextSlider(
        "$NUM_OF_EDIT_TEXT : $numberOfEdit",
        numberOfEdit.toFloat(),
        1f..10f,
    ) {
        numberOfEdit = it.roundToInt()
    }

    TextSlider(
        "$NUM_OF_VARIATION_TEXT : $numberOfVariation",
        numberOfVariation.toFloat(),
        1f..10f,
    ) {
        numberOfVariation = it.roundToInt()
    }

    TextField(
        translationPrompt,
        { translationPrompt = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(TRANSLATE_TO_ENGLISH_PROMPT_TEXT) },
    )
}
