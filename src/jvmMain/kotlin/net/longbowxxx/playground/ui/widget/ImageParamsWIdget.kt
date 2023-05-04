/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import net.longbowxxx.playground.viewmodel.imageProperties
import kotlin.math.roundToInt

private const val NUM_OF_CREATE_TEXT = "NUMBER OF CREATE"
private const val NUM_OF_EDIT_TEXT = "NUMBER OF EDIT"
private const val NUM_OF_VARIATION_TEXT = "NUMBER OF VARIATION"

@Suppress("FunctionName")
@Composable
fun ImageParamsWidget() {
    var numberOfCreate by remember { imageProperties.numberOfCreate }
    var numberOfEdit by remember { imageProperties.numberOfEdit }
    var numberOfVariation by remember { imageProperties.numberOfVariation }

    TextSlider(
        "$NUM_OF_CREATE_TEXT : $numberOfCreate",
        numberOfCreate.toFloat(),
        1f..10f,
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
}
