/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.utils

import java.io.PrintStream

/**
 * Debug loggable interface.
 */
interface DebugLoggable

/**
 * Log message with trace level.
 *
 * @param throwable Throwable.
 * @param lazyMessage Log message.
 */
fun DebugLoggable.logTrace(
    throwable: Throwable? = null,
    lazyMessage: () -> String,
) {
    log(DebugLogLevel.TRACE, this.javaClass.simpleName, throwable, lazyMessage)
}

/**
 * Log message with debug level.
 *
 * @param throwable Throwable.
 * @param lazyMessage Log message.
 */
fun DebugLoggable.logDebug(
    throwable: Throwable? = null,
    lazyMessage: () -> String,
) {
    log(DebugLogLevel.DEBUG, this.javaClass.simpleName, throwable, lazyMessage)
}

/**
 * Log message with info level.
 *
 * @param throwable Throwable.
 * @param lazyMessage Log message.
 */
fun DebugLoggable.logInfo(
    throwable: Throwable? = null,
    lazyMessage: () -> String,
) {
    log(DebugLogLevel.INFO, this.javaClass.simpleName, throwable, lazyMessage)
}

/**
 * Log message with warn level.
 *
 * @param throwable Throwable.
 * @param lazyMessage Log message.
 */
fun DebugLoggable.logWarn(
    throwable: Throwable? = null,
    lazyMessage: () -> String,
) {
    log(DebugLogLevel.WARN, this.javaClass.simpleName, throwable, lazyMessage)
}

/**
 * Log message with error level.
 *
 * @param throwable Throwable.
 * @param lazyMessage Log message.
 */
fun DebugLoggable.logError(
    throwable: Throwable? = null,
    lazyMessage: () -> String,
) {
    log(DebugLogLevel.ERROR, this.javaClass.simpleName, throwable, lazyMessage)
}

/**
 * Debug log output level.
 */
var debugLogOutputLevel: DebugLogLevel = DebugLogLevel.TRACE

/**
 * Debug log output stream.
 */
var debugLogOut: PrintStream? = System.out

/**
 * Debug log error output stream.
 */
var debugLogErrorOut: PrintStream? = System.err

/**
 * Debug log level.
 */
enum class DebugLogLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR,
}

/**
 * Log message.
 *
 * @param level Log level.
 * @param component Log component.
 * @param throwable Throwable.
 * @param lazyMessage Log message.
 */
fun log(
    level: DebugLogLevel,
    component: String,
    throwable: Throwable? = null,
    lazyMessage: () -> String,
) {
    if (level.ordinal < debugLogOutputLevel.ordinal) {
        return
    }
    val out =
        if (level.ordinal >= DebugLogLevel.WARN.ordinal) {
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
