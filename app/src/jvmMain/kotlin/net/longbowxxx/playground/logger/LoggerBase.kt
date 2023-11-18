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

/**
 * Base class for logger.
 *
 * @constructor
 * @param appDataDir Application data directory.
 * @param logCategory Log category.
 */
open class LoggerBase(
    appDataDir: File,
    logCategory: String,
) : Closeable {
    companion object {
        private const val LOG_DIR = "log"
        private const val HORIZONTAL_LINE = "\n----------------------------------------\n\n"
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
    }

    /**
     * JSON encoder.
     */
    protected val encodeJson =
        Json {
            encodeDefaults = false
            prettyPrint = true
        }
    private val writer: BufferedWriter
    protected val dateTimeStr: String
    val logDir: File

    init {
        val now = LocalDateTime.now()
        dateTimeStr = now.format(dateTimeFormatter)
        val categoryDir = File(appDataDir, "$LOG_DIR/$logCategory")
        logDir =
            File(categoryDir, dateTimeStr).apply {
                mkdirs()
            }
        val outFile = File(logDir, "logFile_$dateTimeStr.md")
        writer = outFile.bufferedWriter(Charsets.UTF_8)
    }

    /**
     * Write log.
     *
     * @param block Write block.
     * @return Result of block.
     */
    protected suspend fun <T> writeLog(block: BufferedWriter.() -> T): T {
        return withContext(Dispatchers.IO) {
            writer.block().also {
                writer.flush()
            }
        }
    }

    /**
     * Write error log.
     *
     * @param throwable Throwable.
     */
    suspend fun logError(throwable: Throwable) {
        writeLog {
            write("# ERROR\n")
            write("$throwable\n")
            appendHorizontalLine()
        }
    }

    /**
     * append horizontal line.
     */
    protected fun BufferedWriter.appendHorizontalLine() {
        write(HORIZONTAL_LINE)
    }

    override fun close() {
        writer.close()
    }
}
