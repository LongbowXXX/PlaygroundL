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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

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
        private const val CREATE_IMAGE_URL = "https://api.openai.com/v1/images/generations"
        private val mediaType = "application/json; charset=utf-8".toMediaType()
        private const val DATA_PREFIX = "data: "
        private const val DATA_DONE = "[DONE]"
    }

    override fun requestChatWithStreaming(request: OpenAiChatRequest): Flow<OpenAiChatStreamResponse> {
        val requestBody = encodeJson.encodeToString(request).also { requestJson ->
            logOpenAiRequest { requestJson }
        }.toRequestBody(mediaType)
        val httpRequest = Request.Builder()
            .url(settings.baseUrl)
            .post(requestBody)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${settings.apiKey}")
            .build()

        val client = OkHttpClient()

        val response = client.newCall(httpRequest).execute()

        return response.toFlow()
    }

    override suspend fun requestCreateImage(request: OpenAiCreateImageRequest): OpenAiImageResponse {
        return withContext(Dispatchers.IO) {
            val requestBody = encodeJson.encodeToString(request).also { requestJson ->
                logOpenAiRequest { requestJson }
            }.toRequestBody(mediaType)
            val httpRequest = Request.Builder()
                .url(CREATE_IMAGE_URL)
                .post(requestBody)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer ${settings.apiKey}")
                .build()

            val client = OkHttpClient()

            val response = client.newCall(httpRequest).execute()
            if (response.isSuccessful) {
                response.body?.let { responseBody ->
                    responseBody.byteStream().bufferedReader(Charsets.UTF_8).use { reader ->
                        val allData = reader.readText()
                        decodeJson.decodeFromString<OpenAiImageResponse>(allData)
                    }
                } ?: throw IOException("Create image request failed. Response body is null.")
            } else {
                throw IOException("Create image request failed. ${response.code}")
            }
        }
    }

    private fun Response.toFlow(): Flow<OpenAiChatStreamResponse> = flow {
        val response = this@toFlow
        if (response.isSuccessful) {
            response.body?.let { responseBody ->
                responseBody.byteStream().use { inputStream ->
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
            } ?: throw IOException("response body is null.")
        } else {
            throw IOException("response failed. ${response.code}")
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
