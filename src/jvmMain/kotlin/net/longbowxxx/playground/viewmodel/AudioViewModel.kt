/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.viewmodel

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.longbowxxx.openai.client.OpenAiAudioRequest
import net.longbowxxx.openai.client.OpenAiClient
import net.longbowxxx.openai.client.OpenAiSettings
import net.longbowxxx.playground.recorder.Recorder
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

class AudioViewModel(dispatcher: CoroutineDispatcher = Dispatchers.Default) : CoroutineScope, Closeable {
    private val job = Job()
    override val coroutineContext: CoroutineContext = dispatcher + job

    enum class State {
        STOPPED,
        RECORDING,
        REQUESTING,
    }

    val state = mutableStateOf(State.STOPPED)

    fun startRecording() {
        launch {
            val recorder = Recorder()
            val audioData = recorder.recordAudio(5_000)
            val client = OpenAiClient(OpenAiSettings(appProperties.apiKey))

            val request = OpenAiAudioRequest(
                audioData,
            )
            val response = client.requestAudioTranscription(request)
            println("startRecording $response")
        }
    }

    fun stopRecording() {
    }

    override fun close() {
        runBlocking {
            job.cancelAndJoin()
        }
    }
}
