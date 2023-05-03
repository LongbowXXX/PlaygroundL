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

        private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
    }

    protected val encodeJson = Json {
        encodeDefaults = false
        prettyPrint = true
    }
    protected val writer: BufferedWriter
    protected val dateTimeStr: String
    protected val logDir: File

    init {
        val now = LocalDateTime.now()
        dateTimeStr = now.format(dateTimeFormatter)
        logDir = File(parentDir, dateTimeStr)
        logDir.mkdirs()
        val outFile = File(logDir, "logFile_$dateTimeStr.md")
        writer = outFile.bufferedWriter(Charsets.UTF_8)
    }

    suspend fun logError(throwable: Throwable) {
        withContext(Dispatchers.IO) {
            writer.write("# ERROR\n")
            writer.write("$throwable\n")
            writer.write(HORIZONTAL_LINE)
        }
    }

    override fun close() {
        writer.close()
    }
}

const val LOG_DIR = "log"
const val HORIZONTAL_LINE = "\n----------------------------------------\n\n"
