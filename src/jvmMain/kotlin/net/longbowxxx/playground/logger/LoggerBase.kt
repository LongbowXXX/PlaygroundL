/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.logger

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.BufferedWriter
import java.io.Closeable
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class LoggerBase(
    parentDir: String,
) : Closeable {
    companion object {
        const val LOG_DIR = "log"
        private const val HORIZONTAL_LINE = "\n----------------------------------------\n\n"
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
    }

    protected val encodeJson = Json {
        encodeDefaults = false
        prettyPrint = true
    }
    private val writer: BufferedWriter
    protected val dateTimeStr: String
    val logDir: File

    init {
        val now = LocalDateTime.now()
        dateTimeStr = now.format(dateTimeFormatter)
        logDir = File(parentDir, dateTimeStr).apply {
            mkdirs()
        }
        val outFile = File(logDir, "logFile_$dateTimeStr.md")
        writer = outFile.bufferedWriter(Charsets.UTF_8)
    }

    protected suspend fun <T> writeLog(block: BufferedWriter.() -> T): T {
        return withContext(Dispatchers.IO) {
            writer.block().also {
                writer.flush()
            }
        }
    }

    suspend fun logError(throwable: Throwable) {
        writeLog {
            write("# ERROR\n")
            write("$throwable\n")
            appendHorizontalLine()
        }
    }

    protected fun BufferedWriter.appendHorizontalLine() {
        write(HORIZONTAL_LINE)
    }

    override fun close() {
        writer.close()
    }
}
