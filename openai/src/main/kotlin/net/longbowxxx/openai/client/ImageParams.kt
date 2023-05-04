/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.openai.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class OpenAiCreateImageRequest(
    val prompt: String,
    val n: Int = 1,
    val size: OpenAiSizeTypes = OpenAiSizeTypes.SIZE_1024,
    @SerialName("response_format")
    val responseFormat: OpenAiImageResponseFormatTypes = OpenAiImageResponseFormatTypes.URL,
    val user: String? = null,
)

@Serializable
enum class OpenAiSizeTypes {
    @SerialName("256x256")
    SIZE_256,

    @SerialName("512x512")
    SIZE_512,

    @SerialName("1024x1024")
    SIZE_1024,
}

val OpenAiSizeTypes.requestValue: String
    get() {
        return when (this) {
            OpenAiSizeTypes.SIZE_256 -> "256x256"
            OpenAiSizeTypes.SIZE_512 -> "512x512"
            OpenAiSizeTypes.SIZE_1024 -> "1024x1024"
        }
    }

@Serializable
enum class OpenAiImageResponseFormatTypes {
    @SerialName("url")
    URL,

    @SerialName("b64_json")
    B64_JSON,
}

@Serializable
data class OpenAiImageResponse(
    val created: Int,
    val data: List<OpenAiImageData>,
)

@Serializable
data class OpenAiImageData(
    val url: String? = null,
    @SerialName("b64_json")
    val b64Json: String? = null,
)

data class OpenAiEditImageRequest(
    val image: File,
    val mask: File?,
    val prompt: String,
    val n: Int = 1,
    val size: OpenAiSizeTypes = OpenAiSizeTypes.SIZE_1024,
    val responseFormat: OpenAiImageResponseFormatTypes = OpenAiImageResponseFormatTypes.URL,
    val user: String? = null,
)
