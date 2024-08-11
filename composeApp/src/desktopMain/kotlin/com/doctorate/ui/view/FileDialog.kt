package com.doctorate.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import doctorateui.composeapp.generated.resources.Res
import doctorateui.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import java.awt.FileDialog
import java.awt.FileDialog.LOAD
import java.awt.Frame
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter

/**
 * ClassName: FileDialog
 * Package: com.doctorate.ui.view
 * Description:
 * @author Raincc
 * @Create 2024/8/10 13:29
 * @Version 1.0
 */
class FileFilterImpl(val type: String) : FileFilter() {
    override fun accept(f: File?): Boolean {
        return f?.isDirectory == true || f?.name?.endsWith(type) == true
    }

    override fun getDescription(): String? {
        return type
    }
}

@Composable
fun FileDialog(
    parent: Frame? = null,
    onCloseRequest: (result: String?) -> Unit
) {
    val customImage = painterResource(Res.drawable.compose_multiplatform).toAwtImage(
        density = Density(1f),
        layoutDirection = LayoutDirection.Rtl
    )
    AwtWindow(
        create = {
            object : FileDialog(parent, "Choose a file", LOAD) {
                override fun setVisible(value: Boolean) {
                    super.setVisible(value)
                    if (value) {
                        onCloseRequest(directory?.plus(file))
                    }
                }
            }.apply {
                this.setIconImage(customImage)
            }
        },
        dispose = FileDialog::dispose,
    ) {
    }
}

@Composable
fun JFileChooseDialog(
    type: String,
    onCloseRequest: (String?) -> Unit
) {
    DialogWindow(
        onCloseRequest = { onCloseRequest(null) },
        title = "Choose a file",
        icon = painterResource(Res.drawable.compose_multiplatform),
        resizable = false,
        state = DialogState(
            size = DpSize(800.dp, 600.dp),
            position = WindowPosition(Alignment.Center)
        )
    ) {
        SwingPanel(
            modifier = Modifier.background(Color.Black).fillMaxSize(),
            factory = {
                JFileChooser().apply {
                    this.fileSelectionMode = JFileChooser.FILES_ONLY
                    this.fileFilter = FileFilterImpl(type)
                }
            },
            update = { chooser ->
                chooser.addActionListener {
                    if (it.actionCommand == JFileChooser.APPROVE_SELECTION) {
                        chooser.selectedFile?.let { onCloseRequest(it.absolutePath) }
                    } else if (it.actionCommand == JFileChooser.CANCEL_SELECTION) {
                        onCloseRequest(null)
                    }
                }
            }

        )
    }
}