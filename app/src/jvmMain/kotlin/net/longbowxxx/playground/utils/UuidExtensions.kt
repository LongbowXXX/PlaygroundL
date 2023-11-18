/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.utils

import java.util.UUID

/**
 * Generate random short id.
 *
 * @return Random short id.
 */
fun randomShortId(): String {
    return UUID.randomUUID().toString().substring(0, 8)
}
