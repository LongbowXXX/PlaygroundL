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
import java.util.concurrent.atomic.AtomicBoolean
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import kotlin.math.abs

class Recorder {
    companion object {
        private const val DEFAULT_RECORD_MAX_DURATION_MILLIS = 60_000L
        private const val DEFAULT_SILENCE_DURATION_THRESHOLD_MILLIS = 1_000L
        private const val SAMPLE_RATE = 16000f
        private const val SAMPLE_SIZE_BITS = 16
        private const val CHANNELS = 1
        private const val BUFFER_SIZE = 1024
        private const val SILENCE_AMP_THRESHOLD = 30
        private const val NOISY_DURATION_THRESHOLD = 20

        fun isMicAvailable(): Boolean {
            return runCatching {
                val format = AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_BITS, CHANNELS, true, false)
                val info = DataLine.Info(TargetDataLine::class.java, format)
                // 対応デバイスがないと例外を投げる
                AudioSystem.getLine(info)
            }.isSuccess
        }
    }

    private val stopRequested = AtomicBoolean(false)

    suspend fun recordAudio(
        maxDurationMillis: Long = DEFAULT_RECORD_MAX_DURATION_MILLIS,
        silenceThresholdMillis: Long = DEFAULT_SILENCE_DURATION_THRESHOLD_MILLIS,
    ): ByteArray {
        stopRequested.set(false)
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
                            var silenceDurationMillis = 0
                            var soundDurationMillis = 0
                            var soundDetected = false
                            while (isActive && !stopRequested.get() && silenceDurationMillis < silenceThresholdMillis) {
                                ensureActive()
                                val count = line.read(buffer, 0, buffer.size)
                                if (count > 0) {
                                    outputStream.write(buffer, 0, count)

                                    // Check if the amplitude is below a certain threshold
                                    val amplitude = buffer.maxOfOrNull { abs(it.toInt()) } ?: 0
                                    val deltaMillis = count * 1000 / format.frameSize / format.sampleRate.toInt()
                                    if (amplitude < SILENCE_AMP_THRESHOLD) {
                                        if (soundDetected) {
                                            // 無音区間の計測は、一度以上音の入力があった後
                                            silenceDurationMillis += deltaMillis
                                        }
                                        soundDurationMillis = 0
                                    } else {
                                        soundDurationMillis += deltaMillis
                                        // 一瞬ノイズが入ることもあるので、一定区間音の入力があってはじめて
                                        // 音声入力があったと判断して無音区間をリセット
                                        if (soundDurationMillis > NOISY_DURATION_THRESHOLD) {
                                            silenceDurationMillis = 0
                                            soundDetected = true
                                        }
                                    }
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

    fun stopRecord() {
        stopRequested.set(true)
    }

    private fun addWaveHeader(
        audioData: ByteArray,
        format: AudioFormat,
    ): ByteArray {
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
