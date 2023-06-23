/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.utils

import java.util.*

fun randomShortId(): String {
    return UUID.randomUUID().toString().substring(0, 8)
}
