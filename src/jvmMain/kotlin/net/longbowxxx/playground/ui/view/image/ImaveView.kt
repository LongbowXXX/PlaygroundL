/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.view.image

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Suppress("FunctionName")
@Composable
fun RowScope.ImageView() {
    Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
        ImageListView()
        ImageEditView()
    }
}
