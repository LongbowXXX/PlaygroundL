/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground

import java.io.File

// Constant for the application name
const val APP_NAME = "PlaygroundL"

/**
 * This is a lazy property for the application's data directory.
 * It uses the "APPDATA" environment variable to determine the base directory.
 * If the directory does not exist, it is created.
 * The directory path is "$APPDATA/$APP_NAME/".
 */
val appDataDirectory: File by lazy {
    val appDataPath = System.getenv("APPDATA")
    File("$appDataPath/$APP_NAME/").apply {
        if (!exists()) {
            // Check if the directory was created successfully
            check(mkdirs()) { "Application data file directory create failed. ${this.absolutePath}" }
        }
    }
}
