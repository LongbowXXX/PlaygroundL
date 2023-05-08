/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.recorder

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine

class Recorder {
    companion object {
        private const val SAMPLE_RATE = 16000f
        private const val SAMPLE_SIZE_BITS = 16
        private const val CHANNELS = 1
        private const val BUFFER_SIZE = 1024
    }

    suspend fun recordAudio(maxDurationMillis: Long): ByteArray {
        return withContext(Dispatchers.IO) {
            val format = AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_BITS, CHANNELS, true, false)
            val info = DataLine.Info(TargetDataLine::class.java, format)
            val line = AudioSystem.getLine(info) as TargetDataLine

            val buffer = ByteArray(BUFFER_SIZE)
            ByteArrayOutputStream().use { outputStream ->
                line.open()
                runCatching {
                    line.use {
                        line.start()

                        withTimeout(maxDurationMillis) {
                            while (isActive) {
                                ensureActive()
                                val count = line.read(buffer, 0, buffer.size)
                                if (count > 0) {
                                    outputStream.write(buffer, 0, count)
                                }
                            }
                        }

                        line.stop()
                    }
                }.recover {
                    if (it is CancellationException) {
                        Result.success(Unit)
                    } else {
                        throw it
                    }
                }.getOrThrow()

                addWaveHeader(outputStream.toByteArray(), format)
            }
        }
    }

    private fun addWaveHeader(audioData: ByteArray, format: AudioFormat): ByteArray {
        val headerSize = 44
        val totalSize = headerSize + audioData.size
        val byteRate = format.sampleSizeInBits * format.channels * format.sampleRate.toInt() / 8
        val blockAlign = format.sampleSizeInBits * format.channels / 8

        val header = ByteBuffer.allocate(headerSize)
        header.order(ByteOrder.LITTLE_ENDIAN)
        header.put("RIFF".toByteArray(Charsets.US_ASCII))
        header.putInt(totalSize - 8)
        header.put("WAVE".toByteArray(Charsets.US_ASCII))
        header.put("fmt ".toByteArray(Charsets.US_ASCII))
        header.putInt(16)
        header.putShort(1)
        header.putShort(format.channels.toShort())
        header.putInt(format.sampleRate.toInt())
        header.putInt(byteRate)
        header.putShort(blockAlign.toShort())
        header.putShort(format.sampleSizeInBits.toShort())
        header.put("data".toByteArray(Charsets.US_ASCII))
        header.putInt(audioData.size)

        return ByteArrayOutputStream().use { outputStream ->
            outputStream.write(header.array())
            outputStream.write(audioData)
            outputStream.toByteArray()
        }
    }
}
