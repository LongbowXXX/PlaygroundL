/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.openai.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.util.concurrent.TimeUnit

class OpenAiClientImpl(
    private val settings: OpenAiSettings,
) : OpenAiClient {
    companion object {
        private val encodeJson = Json {
            encodeDefaults = false
            prettyPrint = true
        }
        private val decodeJson = Json {
            encodeDefaults = false
            ignoreUnknownKeys = true
        }
        private const val OPENAI_CHAT_URL = "https://api.openai.com/v1/chat/completions"
        private const val CREATE_IMAGE_URL = "https://api.openai.com/v1/images/generations"
        private const val EDIT_IMAGE_URL = "https://api.openai.com/v1/images/edits"
        private const val IMAGE_VARIATION_URL = "https://api.openai.com/v1/images/variations"
        private const val AUDIO_TRANSCRIPTION_URL = "https://api.openai.com/v1/audio/transcriptions"
        private const val AUDIO_TRANSLATION_URL = "https://api.openai.com/v1/audio/translations"
        private val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        private val imagePngMediaType = "image/png".toMediaType()
        private val audioWavType = "audio/wav".toMediaType()
        private const val DATA_PREFIX = "data: "
        private const val DATA_DONE = "[DONE]"
        private const val TIMEOUT_SECONDS = 30L
    }

    override fun requestChatWithStreaming(request: OpenAiChatRequest): Flow<OpenAiChatStreamResponse> {
        val requestBody = encodeJson.encodeToString(request).also { requestJson ->
            logOpenAiRequest { requestJson }
        }.toRequestBody(jsonMediaType)
        val httpRequest = Request.Builder()
            .url(OPENAI_CHAT_URL)
            .post(requestBody)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${settings.apiKey}")
            .build()

        return httpClient().newCall(httpRequest)
            .execute()
            .successfulBodyOrThrow { responseBody ->
                responseBody.toFlow()
            }
    }

    override suspend fun requestCreateImage(request: OpenAiCreateImageRequest): OpenAiImageResponse {
        return withContext(Dispatchers.IO) {
            val requestBody = encodeJson.encodeToString(request).also { requestJson ->
                logOpenAiRequest { requestJson }
            }.toRequestBody(jsonMediaType)
            val httpRequest = Request.Builder()
                .url(CREATE_IMAGE_URL)
                .post(requestBody)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer ${settings.apiKey}")
                .build()

            httpClient().newCall(httpRequest)
                .execute()
                .successfulBodyOrThrow { responseBody ->
                    decodeJson.decodeFromString<OpenAiImageResponse>(responseBody.string())
                }
        }
    }

    override suspend fun requestEditImage(request: OpenAiEditImageRequest): OpenAiImageResponse {
        return withContext(Dispatchers.IO) {
            val requestBody = MultipartBody.Builder().apply {
                setType(MultipartBody.FORM)
                addFormDataPart("image", request.image.name, request.image.asRequestBody(imagePngMediaType))
                request.mask?.let { mask ->
                    addFormDataPart("mask", mask.name, mask.asRequestBody(imagePngMediaType))
                }
                addFormDataPart("prompt", request.prompt)
                addFormDataPart("n", request.n.toString())
                addFormDataPart("size", request.size.requestValue)
            }.build()

            val httpRequest = Request.Builder()
                .url(EDIT_IMAGE_URL)
                .post(requestBody)
                .header("Authorization", "Bearer ${settings.apiKey}")
                .build()

            httpClient().newCall(httpRequest)
                .execute()
                .successfulBodyOrThrow { responseBody ->
                    decodeJson.decodeFromString<OpenAiImageResponse>(responseBody.string())
                }
        }
    }

    override suspend fun requestImageVariation(request: OpenAiImageVariationRequest): OpenAiImageResponse {
        return withContext(Dispatchers.IO) {
            val requestBody = MultipartBody.Builder().apply {
                setType(MultipartBody.FORM)
                addFormDataPart("image", request.image.name, request.image.asRequestBody(imagePngMediaType))
                addFormDataPart("n", request.n.toString())
                addFormDataPart("size", request.size.requestValue)
            }.build()

            val httpRequest = Request.Builder()
                .url(IMAGE_VARIATION_URL)
                .post(requestBody)
                .header("Authorization", "Bearer ${settings.apiKey}")
                .build()

            httpClient().newCall(httpRequest)
                .execute()
                .successfulBodyOrThrow { responseBody ->
                    decodeJson.decodeFromString<OpenAiImageResponse>(responseBody.string())
                }
        }
    }

    override suspend fun requestAudioTranscription(request: OpenAiAudioRequest): OpenAiAudioResponse {
        return requestAudio(request, AUDIO_TRANSCRIPTION_URL)
    }

    override suspend fun requestAudioTranslation(request: OpenAiAudioRequest): OpenAiAudioResponse {
        return requestAudio(request, AUDIO_TRANSLATION_URL)
    }

    private suspend fun requestAudio(request: OpenAiAudioRequest, endpoint: String): OpenAiAudioResponse {
        return withContext(Dispatchers.IO) {
            val requestBody = MultipartBody.Builder().apply {
                setType(MultipartBody.FORM)
                addFormDataPart("file", "file.wav", request.wavData.toRequestBody(audioWavType))
                addFormDataPart("model", request.model)
                request.prompt?.let {
                    addFormDataPart("prompt", it)
                }
                request.responseFormat?.let {
                    addFormDataPart("response_format", it.requestValue)
                }
                request.temperature?.let {
                    addFormDataPart("temperature", it.toString())
                }
            }.build()

            val httpRequest = Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .header("Authorization", "Bearer ${settings.apiKey}")
                .build()

            httpClient().newCall(httpRequest)
                .execute()
                .successfulBodyOrThrow { responseBody ->
                    decodeJson.decodeFromString<OpenAiAudioResponse.Json>(responseBody.string())
                }
        }
    }

    private fun httpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        }.build()
    }

    private inline fun <T> Response.successfulBodyOrThrow(block: (ResponseBody) -> T): T {
        return if (isSuccessful) {
            body?.let { responseBody ->
                block(responseBody)
            } ?: throw IOException("Request failed. Response body is null.")
        } else {
            throw IOException("Request failed. $code, ${body?.string()}, $message")
        }
    }

    private fun ResponseBody.toFlow(): Flow<OpenAiChatStreamResponse> = flow {
        byteStream().use { inputStream ->
            inputStream.bufferedReader(Charsets.UTF_8).use { reader ->
                var line: String? = reader.readLine()

                while (line != null) {
                    logOpenAiResponse { line.orEmpty() }
                    line.toStreamResponse()?.let {
                        emit(it)
                    }
                    line = reader.readLine()
                }
            }
        }
    }

    private fun String.toStreamResponse(): OpenAiChatStreamResponse? {
        return if (this.startsWith(DATA_PREFIX)) {
            val data = this.substring(DATA_PREFIX.length)
            if (data != DATA_DONE) {
                decodeJson.decodeFromString<OpenAiChatStreamResponse>(data)
            } else {
                null
            }
        } else {
            null
        }
    }
}
