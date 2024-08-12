package com.doctorate.ui.page.emulator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import com.doctorate.ui.config.AppConfig
import com.doctorate.ui.config.LocalAppConfig
import com.doctorate.ui.config.readConfig
import com.doctorate.ui.util.CommandUtil
import com.doctorate.ui.view.FileDialog
import com.doctorate.ui.view.LocalAppToaster
import com.doctorate.ui.view.Toaster
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.io.File
import java.io.IOException

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
    val onConfigChange = LocalAppConfig.current.onConfigChange
    var commandOutput = rememberSaveable { mutableStateListOf<String>() }
    var adbUri by rememberSaveable { mutableStateOf(config.adbUri) }
    Surface(color = MaterialTheme.colors.background) {
        Column(Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)).background(color = Color.LightGray)) {
            ConnectEmulatorButton(
                adbUri = adbUri,
                adbToolPath = config.adbToolPath,
                serverPort = config.serverPort,
                onCommandUpdate = { commandOutput.add(it) },
                onAdbUriChange = { adbUri = it },
                onAdbUriSave = { onConfigChange(it) },
            )
            CustomButtons(
                config = config,
                onCommandUpdate = { commandOutput.add(it) },
                onConfigChange = onConfigChange
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(8.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(color = Color.Black)
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                items(commandOutput) { output -> Text(text = output, color = Color.White) }
            }
        }
    }
}

@Composable
fun ConnectEmulatorButton(
    adbUri: String,
    adbToolPath: String,
    serverPort: Int,
    onAdbUriChange: (String) -> Unit,
    onAdbUriSave: (AppConfig) -> Unit,
    onCommandUpdate: (String) -> Unit,
) {
    val toast = LocalAppToaster.current
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.Gray)
            .padding(4.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            leadingIcon = {
                Text(text = "ADB Uri : ", color = Color.Black, modifier = Modifier.padding(start = 8.dp))
            },
            singleLine = true,
            value = adbUri,
            onValueChange = { onAdbUriChange(it) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.LightGray,
                unfocusedBorderColor = MaterialTheme.colors.onSurface,
                focusedBorderColor = Color.Black,
            ),
            modifier = Modifier.width(460.dp)
        )
        val modifier = Modifier.fillMaxHeight().width(100.dp)
        Button(
            modifier = modifier,
            onClick = {
                val regex =
                    Regex("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]):(?:[0-9]|[1-9][0-9]{1,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])\$")
                CoroutineScope(Dispatchers.IO).launch {
                    if (!regex.matches(adbUri)) {
                        toast.toastFailure("Illegal adb uri")
                        throw RuntimeException("Illegal adb uri")
                    }
                    onAdbUriSave(readConfig().copy(adbUri = adbUri))
                }
            }) {
            Text("Save")
        }
        Button(
            modifier = modifier,
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        CommandUtil.cmdTask(adbToolPath, "connect", adbUri)?.also {
                            if (it.startsWith("cannot")) {
                                throw RuntimeException("Connecting failed, Please check emulator state!")
                            }
                            onCommandUpdate(it)
                        }
                        CommandUtil.cmdTask(adbToolPath, "start-server")
                        CommandUtil.cmdTask(adbToolPath, "wait-for-server")
                        CommandUtil.cmdTask(adbToolPath, "root")
                        CommandUtil.cmdTask(adbToolPath, "reverse", "tcp:$serverPort", "tcp:$serverPort")
                        onCommandUpdate("Python Version : ${CommandUtil.cmdTask("python", "--version")}")
                        onCommandUpdate("Frida Version : ${CommandUtil.cmdTask("frida", "--v")}")
                        try {
                            CommandUtil.cmdAsyncTask(
                                adbToolPath,
                                "shell",
                                "/data/local/tmp/frida-server",
                                "&",
                                onCommandUpdate = onCommandUpdate
                            )
                            onCommandUpdate("running frida-server")
                        } catch (_: IOException) {
                            throw RuntimeException("Frida server on emulator not exists")
                        }
                    } catch (e: Exception) {
                        onCommandUpdate(e.message.toString().also { toast.toastFailure(it) })
                    }
                }

            }) {
            Text("Connect Emulator")
        }
    }
}

@Composable
fun CustomButtons(
    config: AppConfig,
    onCommandUpdate: (String) -> Unit,
    onConfigChange: (AppConfig) -> Unit,
) {
    val toast = LocalAppToaster.current
    var showEmulatorDialog by remember { mutableStateOf(false) }
    var showScriptDialog by remember { mutableStateOf(false) }
    var showPackageDialog by remember { mutableStateOf(false) }
    var showCustomDialog by remember { mutableStateOf(false) }
    var appPackage by rememberSaveable { mutableStateOf(config.appPackageName) }
    var scriptState by rememberSaveable { mutableStateOf(File(config.scriptPath).exists()) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp).height(56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { showEmulatorDialog = true }, modifier = Modifier.size(100.dp),
        ) {
            Text("Open Emulator")
            if (showEmulatorDialog) {
                if (config.emulatorPath.isNotEmpty()) {
                    showEmulatorDialog = false
                    launchEmulator(config.emulatorPath, onCommandUpdate)
                } else {
                    FileDialog(
                        onCloseRequest = {
                            showEmulatorDialog = false
                            it?.also {
                                if (it.endsWith(".exe")) {
                                    launchEmulator(
                                        it.also { onConfigChange(config.copy(emulatorPath = it)) },
                                        onCommandUpdate
                                    )
                                } else {
                                    onCommandUpdate("Error file,Please try again")
                                }
                            }
                        }
                    )
                }
            }
        }
        Button(onClick = { showScriptDialog = true }, modifier = Modifier.size(100.dp)) {
            Text(text = "Select Script", modifier = Modifier.align(Alignment.CenterVertically))
            if (showScriptDialog) {
                FileDialog(
                    onCloseRequest = {
                        showScriptDialog = false
                        it?.also {
                            if (it.endsWith(".js")) {
                                scriptState = true
                                File(it).copyTo(File(config.scriptPath), true).also { onConfigChange(config) }
                            } else {
                                onCommandUpdate("Error file,Please try again")
                            }
                        }
                    }
                )
            }
        }
        Button(
            onClick = { showPackageDialog = true },
            enabled = scriptState,
            modifier = Modifier.size(100.dp)
        ) {
            Text("Launch Game")
            if (showPackageDialog) {
                if (config.appPackageName.isNotEmpty()) {
                    showPackageDialog = false
                    launchGame(config, toast, onCommandUpdate)
                } else {
                    DialogWindow(
                        onCloseRequest = { showPackageDialog = false },
                        resizable = false,
                        title = "Enter appPackage name",
                        state = DialogState(
                            size = DpSize(400.dp, 120.dp),
                            position = WindowPosition(Alignment.Center)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            OutlinedTextField(
                                singleLine = true,
                                value = appPackage,
                                onValueChange = { appPackage = it },
                                modifier = Modifier.width(280.dp).fillMaxHeight()
                            )
                            Spacer(Modifier.width(16.dp))
                            Button(
                                onClick = {
                                    if (appPackage.isNotEmpty()) {
                                        showPackageDialog = false
                                        onConfigChange(
                                            config.copy(appPackageName = appPackage)
                                                .also { launchGame(it, toast, onCommandUpdate) })
                                    }
                                },
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Text("Save")
                            }
                        }
                    }
                }
            }
        }
        Button(
            onClick = { showCustomDialog = true },
            modifier = Modifier.size(100.dp)
        ) {
            Text("Custom")
            if (showCustomDialog) {
                if (config.customPath.isNotEmpty()) {
                    showCustomDialog = false
                    launchEmulator(config.customPath, onCommandUpdate)
                } else {
                    FileDialog(
                        onCloseRequest = {
                            showCustomDialog = false
                            println(it)
                            it?.also {
                                onConfigChange(config.copy(customPath = it.also {
                                    launchEmulator(
                                        it,
                                        onCommandUpdate
                                    )
                                }))
                            }
                        }
                    )
                }
            }
        }
    }
}

fun launchEmulator(emulatorPath: String, onCommandUpdate: (String) -> Unit) =
    CoroutineScope(Dispatchers.IO).launch {
        CommandUtil.cmdAsyncTask(emulatorPath, onCommandUpdate = onCommandUpdate)
    }

fun launchGame(config: AppConfig, toast: Toaster, onCommandUpdate: (String) -> Unit) =
    CoroutineScope(Dispatchers.IO).launch {
        try {
            CommandUtil.cmdAsyncTask(
                "frida",
                "-U",
                "-f",
                config.appPackageName,
                "-l",
                config.scriptPath,
                onCommandUpdate = onCommandUpdate
            )
        } catch (_: IOException) {
            onCommandUpdate("Can't find Frida")
            toast.toastFailure("Can't find Frida")
        }
    }
