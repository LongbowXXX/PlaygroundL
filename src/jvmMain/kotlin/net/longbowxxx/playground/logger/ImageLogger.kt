/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.logger

import net.longbowxxx.openai.client.OpenAiCreateImageRequest
import net.longbowxxx.openai.client.OpenAiEditImageRequest
import net.longbowxxx.openai.client.OpenAiImageVariationRequest
import net.longbowxxx.playground.utils.copyTo
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

class ImageLogger(
    appDataDir: File,
    logCategory: String = IMAGE_LOG,
) : LoggerBase(appDataDir, logCategory) {

    companion object {
        const val IMAGE_LOG = "image"
        private const val MASK_FILE_NAME = "mask.png"
        private const val PNG_FILE_FORMAT = "png"
    }

    suspend fun logCreateRequest(request: OpenAiCreateImageRequest) {
        writeLog {
            write("Playground Create Image Log $dateTimeStr\n")
            write("===\n")
            write("# Request Parameters\n")
            write("${request.prompt}\n")
            appendHorizontalLine()
        }
    }

    suspend fun logVariationRequest(request: OpenAiImageVariationRequest) {
        writeLog {
            val orgImage = request.image.copyTo(File(logDir, "original-image.png"))
            val imageName = orgImage.name
            write("Playground Image Variation Log $dateTimeStr\n")
            write("===\n")
            write("# Request Parameters\n")
            write("![$imageName]($imageName \"$imageName\")\n")
            appendHorizontalLine()
        }
    }

    suspend fun logEditImageRequest(request: OpenAiEditImageRequest) {
        writeLog {
            val orgImage = request.image.copyTo(File(logDir, "original-image.png"))
            val imageName = orgImage.name
            write("Playground Edit Image Log $dateTimeStr\n")
            write("===\n")
            write("# Request Parameters\n")
            write("${request.prompt}\n")
            write("![$imageName]($imageName \"$imageName\")\n")
            write("![$MASK_FILE_NAME]($MASK_FILE_NAME \"$MASK_FILE_NAME\")\n")
            appendHorizontalLine()
        }
    }

    suspend fun logImage(index: Int, imageUrl: URL): File {
        return writeLog {
            if (index == 0) {
                write("# Response\n")
            }

            // 画像をファイルに保存する
            val imageName = "image-$index.png"
            val file = File(logDir, imageName)
            imageUrl.copyTo(file)
            // ログに画像表示
            write("![$imageName]($imageName \"$imageName\")\n")
            file
        }
    }

    suspend fun logMaskImage(image: BufferedImage): File {
        return writeLog {
            // 画像をファイルに保存する
            val imageName = MASK_FILE_NAME
            val file = File(logDir, imageName)
            ImageIO.write(image, PNG_FILE_FORMAT, file)
            file
        }
    }
}
