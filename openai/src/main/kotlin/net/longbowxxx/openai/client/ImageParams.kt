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
    val model: String = OPENAI_IMAGE_MODEL_DALL_E_2,
    val n: Int = 1,
    val quality: String? = null,
    val size: OpenAiSizeTypes = OpenAiSizeTypes.SIZE_1024,
    val style: String? = null,
    @SerialName("response_format")
    val responseFormat: OpenAiImageResponseFormatTypes = OpenAiImageResponseFormatTypes.URL,
    val user: String? = null,
) {
    companion object {
        fun ofDallE3(
            prompt: String,
            quality: String = OPENAI_IMAGE_QUALITY_STANDARD,
            size: OpenAiSizeTypes = OpenAiSizeTypes.SIZE_1024,
            style: String = OPENAI_IMAGE_STYLE_VIVID,
            n: Int = 1,
            responseFormat: OpenAiImageResponseFormatTypes = OpenAiImageResponseFormatTypes.URL,
        ): OpenAiCreateImageRequest {
            return OpenAiCreateImageRequest(
                prompt = prompt,
                model = OPENAI_IMAGE_MODEL_DALL_E_3,
                n = n,
                quality = quality,
                size = size,
                style = style,
                responseFormat = responseFormat,
            )
        }
    }
}

const val OPENAI_IMAGE_MODEL_DALL_E_2 = "dall-e-2"
const val OPENAI_IMAGE_MODEL_DALL_E_3 = "dall-e-3"
const val OPENAI_IMAGE_QUALITY_STANDARD = "standard"
const val OPENAI_IMAGE_QUALITY_HD = "hd"
const val OPENAI_IMAGE_STYLE_VIVID = "vivid"
const val OPENAI_IMAGE_STYLE_NATURAL = "natural"

@Serializable
enum class OpenAiSizeTypes {
    @SerialName("256x256")
    SIZE_256,

    @SerialName("512x512")
    SIZE_512,

    @SerialName("1024x1024")
    SIZE_1024,

    @SerialName("1792x1024")
    SIZE_1792X1024,

    @SerialName("1024x1792")
    SIZE_1024X1792,
}

val OpenAiSizeTypes.requestValue: String
    get() {
        return when (this) {
            OpenAiSizeTypes.SIZE_256 -> "256x256"
            OpenAiSizeTypes.SIZE_512 -> "512x512"
            OpenAiSizeTypes.SIZE_1024 -> "1024x1024"
            OpenAiSizeTypes.SIZE_1792X1024 -> "1792x1024"
            OpenAiSizeTypes.SIZE_1024X1792 -> "1024x1792"
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

data class OpenAiImageVariationRequest(
    val image: File,
    val n: Int = 1,
    val size: OpenAiSizeTypes = OpenAiSizeTypes.SIZE_1024,
    val responseFormat: OpenAiImageResponseFormatTypes = OpenAiImageResponseFormatTypes.URL,
    val user: String? = null,
)
