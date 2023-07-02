/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.utils

import java.io.PrintStream

interface DebugLoggable

fun DebugLoggable.logTrace(throwable: Throwable? = null, lazyMessage: () -> String) {
    log(DebugLogLevel.TRACE, this.javaClass.simpleName, throwable, lazyMessage)
}

fun DebugLoggable.logDebug(throwable: Throwable? = null, lazyMessage: () -> String) {
    log(DebugLogLevel.DEBUG, this.javaClass.simpleName, throwable, lazyMessage)
}

fun DebugLoggable.logInfo(throwable: Throwable? = null, lazyMessage: () -> String) {
    log(DebugLogLevel.INFO, this.javaClass.simpleName, throwable, lazyMessage)
}

fun DebugLoggable.logWarn(throwable: Throwable? = null, lazyMessage: () -> String) {
    log(DebugLogLevel.WARN, this.javaClass.simpleName, throwable, lazyMessage)
}

fun DebugLoggable.logError(throwable: Throwable? = null, lazyMessage: () -> String) {
    log(DebugLogLevel.ERROR, this.javaClass.simpleName, throwable, lazyMessage)
}

var debugLogOutputLevel: DebugLogLevel = DebugLogLevel.TRACE
var debugLogOut: PrintStream? = System.out
var debugLogErrorOut: PrintStream? = System.err

enum class DebugLogLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
}

fun log(level: DebugLogLevel, component: String, throwable: Throwable? = null, lazyMessage: () -> String) {
    if (level.ordinal < debugLogOutputLevel.ordinal) {
        return
    }
    val out = if (level.ordinal >= DebugLogLevel.WARN.ordinal) {
        debugLogErrorOut
    } else {
        debugLogOut
    }
    if (throwable != null) {
        out?.println("[$component] ${lazyMessage()}, throwable=$throwable, trance=${throwable.stackTraceToString()}")
    } else {
        out?.println("[$component] ${lazyMessage()}")
    }
}
