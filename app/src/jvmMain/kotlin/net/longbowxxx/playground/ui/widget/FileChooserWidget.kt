/*
 * Copyright (c) 2023 LongbowXXX
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package net.longbowxxx.playground.ui.widget

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Suppress("FunctionName")
@Composable
fun FileChooseWidget(
    label: String,
    onSelected: (String?) -> Unit,
) {
    TextButton({
        val filePath = showFileChooserDialog()
        onSelected(filePath)
    }) {
        Text(label)
    }
}

fun showFileChooserDialog(): String? {
    val fileChooser = JFileChooser()
    val currentDir = System.getProperty("user.dir")
    fileChooser.currentDirectory = File(currentDir)
    val filter = FileNameExtensionFilter("Text Files", "txt", "md")
    fileChooser.fileFilter = filter
    val result = fileChooser.showOpenDialog(null)
    return if (result == JFileChooser.APPROVE_OPTION) {
        fileChooser.selectedFile.path
    } else {
        null
    }
}
