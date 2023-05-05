/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.longbowxxx.openai.client.OPENAI_CHAT_URL
import net.longbowxxx.openai.client.OpenAiChatMessage
import net.longbowxxx.openai.client.OpenAiChatRequest
import net.longbowxxx.openai.client.OpenAiChatRoleTypes
import net.longbowxxx.openai.client.OpenAiChatStreamResponse
import net.longbowxxx.openai.client.OpenAiClient
import net.longbowxxx.openai.client.OpenAiCreateImageRequest
import net.longbowxxx.openai.client.OpenAiEditImageRequest
import net.longbowxxx.openai.client.OpenAiImageVariationRequest
import net.longbowxxx.openai.client.OpenAiSettings
import net.longbowxxx.playground.logger.ImageLogger
import net.longbowxxx.playground.logger.LOG_DIR
import org.jetbrains.skia.Bitmap
import org.jetbrains.skiko.toBitmap
import java.awt.image.BufferedImage
import java.io.Closeable
import java.io.File
import java.net.URL
import javax.imageio.ImageIO
import kotlin.coroutines.CoroutineContext

class ImageViewModel(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : CoroutineScope, Closeable {
    private val job = Job()
    override val coroutineContext: CoroutineContext = dispatcher + job

    companion object {
        private const val DEFAULT_SIZE = 1024
        private const val DEFAULT_STROKE_WIDTH = 32f
    }

    val prompt = mutableStateOf("")
    val promptJa = mutableStateOf("")
    val responseImages = mutableStateOf<List<Pair<Bitmap, File>>>(emptyList())
    val activeImage = mutableStateOf<Pair<Bitmap, File>?>(null)
    val requesting = mutableStateOf(false)
    val requestingTranslation = mutableStateOf(false)
    val errorMessage = mutableStateOf("")
    val maskImage = mutableStateOf(
        BufferedImage(
            DEFAULT_SIZE,
            DEFAULT_SIZE,
            BufferedImage.TYPE_INT_ARGB,
        ),
    )
    val strokeWidth = mutableStateOf(DEFAULT_STROKE_WIDTH)
    var drawLines = mutableStateOf(listOf<Pair<List<Offset>, Float>>())

    fun requestTranslation() {
        launch {
            requestingTranslation.value = true
            runCatching {
                val request = OpenAiChatRequest(
                    chatProperties.chatModel.value,
                    messages = listOf(
                        OpenAiChatMessage(OpenAiChatRoleTypes.SYSTEM, imageProperties.translationPrompt.value),
                        OpenAiChatMessage(OpenAiChatRoleTypes.USER, promptJa.value),
                    ),
                    stream = true,
                    temperature = 0f,
                )
                val client = OpenAiClient(OpenAiSettings(OPENAI_CHAT_URL, appProperties.apiKey))
                client.requestChatWithStreaming(request).correctStreamResponse()
            }.onFailure {
                errorMessage.value = it.message ?: it.toString()
            }.also {
                requestingTranslation.value = false
            }
        }
    }

    private suspend fun Flow<OpenAiChatStreamResponse>.correctStreamResponse() {
        var firstTime = true
        this.collect { streamResponse ->
            if (firstTime) {
                firstTime = false
                prompt.value = ""
            }

            streamResponse.choices.firstOrNull()?.delta?.content?.let { contentDelta ->
                val oldMessage = prompt.value
                prompt.value = oldMessage + contentDelta
            }
        }
    }

    fun requestCreateImage() {
        launch {
            val logger = ImageLogger(LOG_DIR)
            runCatching {
                requesting.value = true
                clearImages()
                val client = OpenAiClient(OpenAiSettings(OPENAI_CHAT_URL, appProperties.apiKey))
                val request = OpenAiCreateImageRequest(
                    prompt.value,
                    n = imageProperties.numberOfCreate.value,
                )
                logger.logCreateRequest(request)

                val response = client.requestCreateImage(request)

                response.data.mapNotNull { imageData -> imageData.url?.toURL() }
                    .mapIndexed { index, imageUrl ->
                        val imageFile = logger.logImage(index, imageUrl)
                        val image = ImageIO.read(imageFile)
                        addImage(image.toBitmap(), imageFile)
                    }
            }.onFailure {
                errorMessage.value = it.toString()
                logger.logError(it)
            }.also {
                requesting.value = false
                logger.close()
            }
        }
    }

    fun requestImageVariation() {
        launch {
            val logger = ImageLogger(LOG_DIR)
            runCatching {
                requesting.value = true
                val requestImageFile = requireNotNull(activeImage.value).second
                clearImages()
                val client = OpenAiClient(OpenAiSettings(OPENAI_CHAT_URL, appProperties.apiKey))
                val request = OpenAiImageVariationRequest(
                    image = requestImageFile,
                    n = imageProperties.numberOfVariation.value,
                )
                logger.logVariationRequest(request)

                val response = client.requestImageVariation(request)
                response.data.mapNotNull { imageData -> imageData.url?.toURL() }
                    .mapIndexed { index, imageUrl ->
                        val imageFile = logger.logImage(index, imageUrl)
                        val image = ImageIO.read(imageFile)
                        addImage(image.toBitmap(), imageFile)
                    }
            }.onFailure {
                errorMessage.value = it.toString()
                logger.logError(it)
            }.also {
                requesting.value = false
                logger.close()
            }
        }
    }

    fun requestEditImage() {
        launch {
            val logger = ImageLogger(LOG_DIR)
            runCatching {
                requesting.value = true

                val maskFile = logger.logMaskImage(maskImage.value.toMaskImage())

                val requestImageFile = requireNotNull(activeImage.value).second
                clearImages()
                val client = OpenAiClient(OpenAiSettings(OPENAI_CHAT_URL, appProperties.apiKey))
                val request = OpenAiEditImageRequest(
                    image = requestImageFile,
                    mask = maskFile,
                    prompt = prompt.value,
                    n = imageProperties.numberOfEdit.value,
                )
                logger.logEditImageRequest(request)

                val response = client.requestEditImage(request)
                response.data.mapNotNull { imageData -> imageData.url?.toURL() }
                    .mapIndexed { index, imageUrl ->
                        val imageFile = logger.logImage(index, imageUrl)
                        val image = ImageIO.read(imageFile)
                        addImage(image.toBitmap(), imageFile)
                    }
            }.onFailure {
                errorMessage.value = it.toString()
                logger.logError(it)
            }.also {
                requesting.value = false
                logger.close()
            }
        }
    }

    private fun BufferedImage.toMaskImage(): BufferedImage {
        return BufferedImage(DEFAULT_SIZE, DEFAULT_SIZE, type).apply {
            createGraphics().apply {
                drawImage(this@toMaskImage, 0, 0, DEFAULT_SIZE, DEFAULT_SIZE, null)
            }.dispose()
        }.apply {
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val rgb = getRGB(x, y)
                    if (rgb and 0xff000000.toInt() == 0x00000000) {
                        // 透過の場合黒色に設定
                        setRGB(x, y, 0xff000000.toInt())
                    } else {
                        // アルファ値を0に設定
                        setRGB(x, y, 0x00000000)
                    }
                }
            }
        }
    }

    private fun String.toURL(): URL {
        return URL(this)
    }

    private fun addImage(image: Bitmap, file: File) {
        val newList = responseImages.value.toMutableList()
        newList.add(image to file)
        responseImages.value = newList

        if (activeImage.value == null) {
            activeImage.value = image to file
        }
    }

    private fun clearImages() {
        activeImage.value = null
        responseImages.value = emptyList()
        maskImage.value = BufferedImage(
            maskImage.value.width,
            maskImage.value.height,
            BufferedImage.TYPE_INT_ARGB,
        )
        drawLines.value = emptyList()
    }

    override fun close() {
        runBlocking {
            job.cancelAndJoin()
        }
    }
}
