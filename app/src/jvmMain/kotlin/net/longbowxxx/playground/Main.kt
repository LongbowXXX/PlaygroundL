/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground

import androidx.compose.ui.window.application
import net.longbowxxx.playground.ui.PlaygroundWindow

/**
 * The main entry point of the application.
 * This function creates a new application and opens the PlaygroundWindow.
 */
fun main() =
    application {
        /**
         * Creates a new PlaygroundWindow.
         * PlaygroundWindow is a Compose window that contains the main UI of the application.
         */
        PlaygroundWindow()
    }
