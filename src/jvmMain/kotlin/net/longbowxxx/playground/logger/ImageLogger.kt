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
import net.longbowxxx.openai.client.OpenAiImageVariationRequest
import java.io.File
import java.net.URL

class ImageLogger(
    parentDir: String,
) : LoggerBase(parentDir) {
    suspend fun logCreateRequest(request: OpenAiCreateImageRequest) {
        withContext(Dispatchers.IO) {
            writer.write("Playground Create Image Log $dateTimeStr\n")
            writer.write("===\n")
            writer.write("# Request Parameters\n")
            writer.write("```json\n")
            val json = encodeJson.encodeToString(request)
            writer.write("$json\n")
            writer.write("```\n")
            writer.write(HORIZONTAL_LINE)
        }
    }

    suspend fun logVariationRequest(request: OpenAiImageVariationRequest) {
        withContext(Dispatchers.IO) {
            val orgImage = request.image.copyTo(File(logDir, "original-image.png"))
            val imageName = orgImage.name
            writer.write("Playground Image Variation Log $dateTimeStr\n")
            writer.write("===\n")
            writer.write("# Request Parameters\n")
            writer.write("![$imageName]($imageName \"$imageName\")\n")
            writer.write(HORIZONTAL_LINE)
        }
    }

    suspend fun logImage(index: Int, imageUrl: URL): File {
        return withContext(Dispatchers.IO) {
            if (index == 0) {
                writer.write("# Response\n")
            }

            // 画像をファイルに保存する
            val imageName = "image-$index.png"
            val file = File(logDir, imageName)
            imageUrl.copyTo(file)
            // ログに画像表示
            writer.write("![$imageName]($imageName \"$imageName\")\n")
            file
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
