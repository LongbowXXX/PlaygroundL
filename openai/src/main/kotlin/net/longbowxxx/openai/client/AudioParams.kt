/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.openai.client

import kotlinx.serialization.Serializable
import java.util.*

const val AUDIO_MODEL = "whisper-1"

@Suppress("ArrayInDataClass")
data class OpenAiAudioRequest(
    val wavData: ByteArray,
    val model: String = AUDIO_MODEL,
    val prompt: String? = null,
    val responseFormat: OpenAiAudioResponseFormatTypes? = null,
    val temperature: Float? = null,
)

@Serializable
enum class OpenAiAudioResponseFormatTypes {
    JSON,
    TEXT,
    SRT,
    VERBOSE_JSON,
    VTT,
}

val OpenAiAudioResponseFormatTypes.requestValue: String
    get() {
        return this.toString().lowercase(Locale.US)
    }

sealed class OpenAiAudioResponse {
    @Serializable
    data class Json(
        val text: String,
    ) : OpenAiAudioResponse()

    data class Text(
        val text: String,
    ) : OpenAiAudioResponse()

    data class Srt(
        val text: String,
    ) : OpenAiAudioResponse()

    data class VerboseJson(
        val text: String,
    ) : OpenAiAudioResponse()

    data class Vtt(
        val text: String,
    ) : OpenAiAudioResponse()
}
