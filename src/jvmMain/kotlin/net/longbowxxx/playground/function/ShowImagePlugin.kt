/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.function

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.longbowxxx.openai.client.OpenAiChatFunction
import net.longbowxxx.openai.client.OpenAiChatParameter
import net.longbowxxx.openai.client.OpenAiChatProperty
import net.longbowxxx.playground.utils.toURL
import org.jetbrains.skiko.toBitmap
import java.io.File
import javax.imageio.ImageIO

class ShowImagePlugin : ChatFunctionPlugin() {
    override val functionSpec: OpenAiChatFunction
        get() = OpenAiChatFunction(
            "show_image",
            "show image",
            OpenAiChatParameter.OpenAiChatParameterObject(
                mapOf(
                    "image_uri" to OpenAiChatProperty("string", "URI to show"),
                ),
                listOf("image_uri"),
            ),
        )

    override suspend fun executeInternal(arguments: String): String {
        return withContext(Dispatchers.IO) {
            val imageArgs = arguments.toParams<ShowImageArgs>()
            ShowImageResponse(SUCCESS, imageArgs.imageUri).toResponseStr()
        }
    }

    @Composable
    override fun FunctionViewInternal(content: String) {
        super.FunctionViewInternal(content)
        val imageResponse = content.toParams<ShowImageResponse>()
        val imageFile = File(imageResponse.imageUri.toURL().toURI())
        val imageBitmap = ImageIO.read(imageFile).toBitmap().asComposeImageBitmap()
        Image(
            bitmap = imageBitmap,
            contentDescription = "Image",
            modifier = Modifier.fillMaxSize().padding(10.dp),
            // 画像のサイズを指定する
            contentScale = ContentScale.Fit,
        )
    }
}

@Serializable
data class ShowImageArgs(
    @SerialName("image_uri")
    val imageUri: String,
)

@Serializable
data class ShowImageResponse(
    val type: String,
    @SerialName("image_uri")
    val imageUri: String,
)
