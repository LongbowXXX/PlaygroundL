/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.utils

import java.io.File

const val APP_NAME = "PlaygroundL"

/**
 * Data directory for this app.
 */
val appDataDirectory: File by lazy {
    val appDataPath = System.getenv("APPDATA")
    File("$appDataPath/$APP_NAME/").apply {
        if (!exists()) {
            check(mkdirs()) { "Application data file directory create failed. ${this.absolutePath}" }
        }
    }
}
