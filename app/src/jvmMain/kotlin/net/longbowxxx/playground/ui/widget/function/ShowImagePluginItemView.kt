/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget.function

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import net.longbowxxx.openai.client.function.ChatFunctionPlugin
import net.longbowxxx.playground.function.ShowImageResponse
import net.longbowxxx.utils.toURL
import org.jetbrains.skiko.toBitmap
import java.io.File
import javax.imageio.ImageIO

class ShowImagePluginItemView(parent: ChatFunctionPlugin) : ChatFunctionPluginItemView(parent) {
    @Composable
    override fun FunctionViewInternal(content: String) {
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
