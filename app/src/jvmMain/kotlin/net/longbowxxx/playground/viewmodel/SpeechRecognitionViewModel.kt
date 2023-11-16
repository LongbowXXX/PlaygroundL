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
import net.longbowxxx.openai.client.OpenAiAudioResponse
import net.longbowxxx.openai.client.OpenAiClient
import net.longbowxxx.openai.client.OpenAiSettings
import net.longbowxxx.playground.recorder.Recorder
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

/**
 * ViewModel for OpenAI speech recognition function.
 */
class SpeechRecognitionViewModel(dispatcher: CoroutineDispatcher = Dispatchers.Default) : CoroutineScope, Closeable {
    private val job = Job()
    override val coroutineContext: CoroutineContext = dispatcher + job

    enum class State {
        STOPPED,
        RECORDING,
        REQUESTING,
    }

    val state = mutableStateOf(State.STOPPED)
    val transText = mutableStateOf("")
    val isMicAvailable = Recorder.isMicAvailable()
    private var transJob: Job? = null
    private val recorder = Recorder()

    fun startTrans(isTranscription: Boolean) {
        val lastTransJob = transJob

        lastTransJob?.cancel()

        transJob =
            launch {
                lastTransJob?.join()
                state.value = State.RECORDING
                runCatching {
                    val audioData = recorder.recordAudio()
                    state.value = State.REQUESTING

                    val client = OpenAiClient(OpenAiSettings(appProperties.apiKey))
                    val request =
                        OpenAiAudioRequest(
                            audioData,
                        )
                    val response =
                        when (isTranscription) {
                            true -> client.requestAudioTranscription(request)
                            false -> client.requestAudioTranslation(request)
                        }

                    transText.value = (response as OpenAiAudioResponse.Json).text
                }.also {
                    recorder.stopRecord()
                    state.value = State.STOPPED
                }
            }
    }

    fun stopRecording() {
        recorder.stopRecord()
    }

    override fun close() {
        runBlocking {
            job.cancelAndJoin()
        }
    }
}
