/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toPainter
import javax.imageio.ImageIO

@Composable
fun secondaryButtonColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary,
    )
}

@Composable
fun tertiaryButtonColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary,
    )
}

@Composable
fun tertiaryIconButtonColors(): IconButtonColors {
    return IconButtonDefaults.iconButtonColors(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary,
    )
}

val appLogoMini: Painter by lazy {
    loadImageResource("app-logo-256.png")
}

val appLogo: Painter by lazy {
    loadImageResource("app-logo.png")
}

private fun loadImageResource(resourceName: String): Painter {
    val resourceUrl = Thread.currentThread().contextClassLoader.getResource(resourceName)
    return ImageIO.read(resourceUrl).toPainter()
}
