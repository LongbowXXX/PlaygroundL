/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.logger

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import net.longbowxxx.openai.client.OpenAiCreateImageRequest
import java.io.File
import java.net.URL

class ImageLogger(
    parentDir: String,
) : LoggerBase(parentDir) {
    suspend fun logCreateRequest(request: OpenAiCreateImageRequest) {
        withContext(Dispatchers.IO) {
            writer.write("Playground Image Log $dateTimeStr\n")
            writer.write("===\n")
            writer.write("# Request Parameters\n")
            writer.write("```json\n")
            val json = encodeJson.encodeToString(request)
            writer.write("$json\n")
            writer.write("```\n")
            writer.write(HORIZONTAL_LINE)
        }
    }

    suspend fun logResponseImage(images: List<URL>) {
        withContext(Dispatchers.IO) {
            writer.write("# Response\n")

            images.forEachIndexed { index, imageUrl ->
                // 画像をファイルに保存する
                val imageName = "image-$index.png"
                val file = File(logDir, imageName)
                imageUrl.copyTo(file)
                // ログに画像表示
                writer.write("![$imageName]($imageName \"$imageName\")\n")
            }
            writer.write(HORIZONTAL_LINE)
        }
    }

    private fun URL.copyTo(outFile: File) {
        openStream().use { input ->
            outFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}
