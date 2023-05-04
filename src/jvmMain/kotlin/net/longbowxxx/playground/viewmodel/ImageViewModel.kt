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
import net.longbowxxx.openai.client.OPENAI_CHAT_URL
import net.longbowxxx.openai.client.OpenAiClient
import net.longbowxxx.openai.client.OpenAiCreateImageRequest
import net.longbowxxx.openai.client.OpenAiSettings
import net.longbowxxx.playground.logger.ImageLogger
import net.longbowxxx.playground.logger.LOG_DIR
import org.jetbrains.skia.Bitmap
import org.jetbrains.skiko.toBitmap
import java.io.Closeable
import java.net.URL
import javax.imageio.ImageIO
import kotlin.coroutines.CoroutineContext

class ImageViewModel(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : CoroutineScope, Closeable {
    private val job = Job()
    override val coroutineContext: CoroutineContext = dispatcher + job

    val prompt = mutableStateOf("")
    val responseImages = mutableStateOf<List<Bitmap>>(emptyList())
    val requesting = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

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
                    .also { urlList ->
                        launch {
                            logger.logResponseImage(urlList)
                        }
                    }
                    .map { imageUrl ->
                        val image = ImageIO.read(imageUrl)
                        addImage(image.toBitmap())
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

    private fun String.toURL(): URL {
        return URL(this)
    }

    private fun addImage(image: Bitmap) {
        val newList = responseImages.value.toMutableList()
        newList.add(image)
        responseImages.value = newList
    }

    private fun clearImages() {
        responseImages.value = emptyList()
    }

    override fun close() {
        runBlocking {
            job.cancelAndJoin()
        }
    }
}
