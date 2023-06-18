/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.utils

import java.io.File
import java.net.URL

fun URL.copyTo(outFile: File) {
    openStream().use { input ->
        outFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }
}

fun String.toURL(): URL {
    return URL(this)
}
