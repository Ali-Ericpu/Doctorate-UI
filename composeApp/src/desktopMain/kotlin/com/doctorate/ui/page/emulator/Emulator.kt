package com.doctorate.ui.page.emulator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.draw.clip
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
import com.doctorate.ui.CommandUtil
import com.doctorate.ui.config.LocalAppConfig
import com.doctorate.ui.view.LocalAppToaster
import doctorateui.composeapp.generated.resources.Res
import doctorateui.composeapp.generated.resources.compose_multiplatform
import doctorateui.composeapp.generated.resources.icon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.awt.FileDialog
import java.awt.Frame
import java.io.IOException
import javax.swing.JFileChooser

/**
 * ClassName: Emulator
 * Package: com.doctorate.ui.page
 * Description:
 * @author Raincc
 * @Create 2024/8/7 20:19
 * @Version 1.0
 */
@Composable
@Preview
fun Emulator() {
    val config = LocalAppConfig.current.config
    val toast = LocalAppToaster.current
    var showCommand by rememberSaveable { mutableStateOf(false) }
    var showLoadFileDialog by rememberSaveable { mutableStateOf(false) }
    var adbHost by rememberSaveable { mutableStateOf(config.adbHost) }
    var adbPort by rememberSaveable { mutableStateOf(config.adbPort.toString()) }
    var adbOutputString = mutableListOf<String>()
    MaterialTheme {
        Column(Modifier.fillMaxWidth()) {
            ChangeAdbUri(
                adbHost = adbHost,
                adbPort = adbPort,
                onHostChange = { adbHost = it },
                onPortChange = {
                    if (it.isEmpty()) {
                        adbPort = ""
                    } else {
                        try {
                            it.toInt()
                            adbPort = it
                        } catch (_: NumberFormatException) {
                        }
                    }
                },
            )
            Button(onClick = {
                adbOutputString.clear()
                showCommand = true
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        CommandUtil.cmdTask("adb", "devices")
                        CommandUtil.cmdTask("adb", "connect", "$adbHost:$adbPort")?.also { adbOutputString.add(it) }
                        CommandUtil.cmdTask("adb", "start-server")
                        CommandUtil.cmdTask("adb", "wait-for-server")
                        CommandUtil.cmdTask("adb", "reverse", "tcp:8443", "tcp:8443")
                        CommandUtil.cmdTask("adb", "root")
                        adbOutputString.add("running frida-server")
                        CommandUtil.cmdAsyncTask(
                            "adb",
                            "shell",
                            "/data/local/tmp/frida-server",
                            "&",
                            list = adbOutputString
                        )
                    } catch (_: IOException) {
                        adbOutputString.add("Connect error please check root permission")
                        toast.toastFailure("Connect error please check root permission")
                    }
                    try {
                        val version = CommandUtil.cmdTask("python", "--version")
                        adbOutputString.add("Python Version : $version")
                    } catch (_: IOException) {
                        adbOutputString.add("Can't find Python")
                        toast.toastFailure("Can't find Python")
                    }
                    try {
                        val version = CommandUtil.cmdTask("frida", "--v")
                        adbOutputString.add("Frida Version : $version")
                    } catch (_: IOException) {
                        adbOutputString.add("Can't find Frida")
                        toast.toastFailure("Can't find Frida")
                    }
                    try {
                        CommandUtil.cmdAsyncTask(
                            "frida",
                            "-U",
                            "-f",
                            "com.hypergryph.arknights",
                            "-l",
                            "hook.js",
                            list = adbOutputString
                        )
                    } catch (_: IOException) {
                        adbOutputString.add("Can't find Frida")
                        toast.toastFailure("Can't find Frida")
                    }
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("保存并连接")
            }
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = { showLoadFileDialog = !showLoadFileDialog }, modifier = Modifier.fillMaxWidth()) {
                Text("Load File")
            }
            AnimatedVisibility(showLoadFileDialog) {
                jFileDialog {
                    showLoadFileDialog = false
                    println(it)
                }
//                FileDialog(
//                    onCloseRequest = {
//                        showLoadFileDialog = false
//                        println(it)
//                    },
//                )
            }
            AnimatedVisibility(showCommand) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(color = Color.Black)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    items(adbOutputString) { output -> Text(text = output, color = Color.White) }
                }
            }
        }
    }
}

@Composable
fun ChangeAdbUri(
    adbHost: String,
    adbPort: String,
    onHostChange: (String) -> Unit,
    onPortChange: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        value = (adbHost),
        onValueChange = { onHostChange(it) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            unfocusedBorderColor = MaterialTheme.colors.onSurface,
            focusedBorderColor = MaterialTheme.colors.onSurface,
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        value = (adbPort.toString()),
        onValueChange = { onPortChange(it) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            unfocusedBorderColor = MaterialTheme.colors.onSurface,
            focusedBorderColor = MaterialTheme.colors.onSurface,
        )
    )
}

@Composable
fun LoadFileDialog(
    showLoadFileDialog: Boolean,
    onCloseWindow: (String) -> Unit,
) {
    if (showLoadFileDialog) {
        FileDialog(
            onCloseRequest = { onCloseWindow(it!!) }
        )
    }
}

@Composable
private fun FileDialog(
    parent: Frame? = null,
    onCloseRequest: (result: String?) -> Unit
) {
    val customImage = painterResource(Res.drawable.icon).toAwtImage(
        density = Density(10f),
        layoutDirection = LayoutDirection.Ltr
    )
    AwtWindow(
        create = {
            object : FileDialog(parent, "Choose a file", LOAD) {
                override fun setVisible(value: Boolean) {
                    super.setVisible(value)
                    if (value) {
                        onCloseRequest(directory + file)
                    }
                }
            }
        },
        dispose = FileDialog::dispose
    ) {
        it.setIconImage(customImage)
    }
}

@Composable
private fun jFileDialog(
    vararg filter: String,
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
                object : JFileChooser() {
                    override fun setVisible(value: Boolean) {
                        super.setVisible(value)
                        onCloseRequest(this.selectedFile?.absolutePath)
                    }
                }.apply {
                    this.fileSelectionMode = JFileChooser.FILES_ONLY
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
