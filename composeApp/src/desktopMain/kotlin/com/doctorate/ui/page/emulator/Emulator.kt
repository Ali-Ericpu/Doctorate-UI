package com.doctorate.ui.page.emulator

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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
import com.doctorate.ui.CommandUtil
import com.doctorate.ui.config.AppConfig
import com.doctorate.ui.config.LocalAppConfig
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
    var showCommand by rememberSaveable { mutableStateOf(false) }
    var adbOutputString = rememberSaveable { mutableStateListOf<String>() }
    var adbUri by rememberSaveable { mutableStateOf(config.adbUri) }
    MaterialTheme {
        Column(Modifier.fillMaxWidth()) {
            ChangeAdbUri(
                adbHost = adbUri,
                onHostChange = { adbUri = it }
            )
            ConnectEmulatorButton(
                adbUri = adbUri,
                onCommandUpdate = { adbOutputString.add(it) },
                onConnect = { showCommand = it }
            )
            CustomButtons(
                config = config,
                onCommandUpdate = { adbOutputString.add(it) }
            )
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
fun ConnectEmulatorButton(
    adbUri: String,
    onCommandUpdate: (String) -> Unit,
    onConnect: (Boolean) -> Unit
) {
    val toast = LocalAppToaster.current
    Button(onClick = {
        val regex =
            Regex("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]):(?:[0-9]|[1-9][0-9]{1,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])\$")
        CoroutineScope(Dispatchers.IO).launch {
            if (!regex.matches(adbUri)) {
                toast.toastFailure("Illegal adb uri")
                throw RuntimeException("Illegal adb uri")
            }
            onConnect(true)
            try {
                CommandUtil.cmdTask("adb", "devices")
                CommandUtil.cmdTask("adb", "connect", adbUri)?.also {
                    if (it.startsWith("cannot")) {
                        toast.toastFailure("Connecting failed, Please check emulator state!")
                        onCommandUpdate("Connecting failed, Please check emulator state!")
                        throw RuntimeException("Connecting failed")
                    }
                    onCommandUpdate(it)
                }
                CommandUtil.cmdTask("adb", "start-server")
                CommandUtil.cmdTask("adb", "wait-for-server")
                CommandUtil.cmdTask("adb", "reverse", "tcp:8443", "tcp:8443")
                CommandUtil.cmdTask("adb", "root")
                onCommandUpdate("running frida-server")
                CommandUtil.cmdAsyncTask(
                    "adb",
                    "shell",
                    "/data/local/tmp/frida-server",
                    "&",
                    onCommandUpdate = onCommandUpdate
                )
            } catch (_: IOException) {
                onCommandUpdate("Connect error please check root permission")
                toast.toastFailure("Connect error please check root permission")
            }
            try {
                val version = CommandUtil.cmdTask("python", "--version")
                onCommandUpdate("Python Version : $version")
            } catch (_: IOException) {
                onCommandUpdate("Can't find Python")
                toast.toastFailure("Can't find Python")
            }
            try {
                val version = CommandUtil.cmdTask("frida", "--v")
                onCommandUpdate("Frida Version : $version")
            } catch (_: IOException) {
                onCommandUpdate("Can't find Frida")
                toast.toastFailure("Can't find Frida")
            }

        }

    }, modifier = Modifier.fillMaxWidth()) {
        Text("保存并连接")
    }
}

@Composable
fun ChangeAdbUri(
    adbHost: String,
    onHostChange: (String) -> Unit
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
}

@Composable
fun CustomButtons(
    config: AppConfig,
    onCommandUpdate: (String) -> Unit
) {
    val toast = LocalAppToaster.current
    var showEmulatorDialog by rememberSaveable { mutableStateOf(false) }
    var showScriptDialog by rememberSaveable { mutableStateOf(false) }
    var showPackageDialog by rememberSaveable { mutableStateOf(false) }
    var appPackage by rememberSaveable { mutableStateOf(config.appPackageName) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp).height(56.dp).padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { showEmulatorDialog = true }, modifier = Modifier.size(100.dp),
        ) {
            Text("Open Emulator")
            if (showEmulatorDialog) {
                if (config.emulatorPath.isNotEmpty()) {
                    showEmulatorDialog = false
                    CommandUtil.cmdTask(config.emulatorPath)
                } else {
                    FileDialog(
                        onCloseRequest = {
                            showEmulatorDialog = false
                            it?.also {
                                if (it.endsWith(".exe")) {
                                    config.emulatorPath = it
                                    CoroutineScope(Dispatchers.IO).launch {
                                        CommandUtil.cmdAsyncTask(it, onCommandUpdate = onCommandUpdate)
                                    }
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
            Text(text = "Load Script", modifier = Modifier.align(Alignment.CenterVertically))
            if (showScriptDialog) {
                FileDialog(
                    onCloseRequest = {
                        showScriptDialog = false
                        it?.also {
                            if (it.endsWith(".js")) {
                                File(it).copyTo(File(config.scriptPath), true)
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
//            enabled = File(config.scriptPath).exists(),
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
                                modifier = Modifier.width(280.dp).fillMaxHeight(),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    textColor = Color.White,
                                    backgroundColor = Color.Gray,
                                    unfocusedBorderColor = MaterialTheme.colors.onSurface,
                                    focusedBorderColor = Color.Cyan
                                )
                            )
                            Spacer(Modifier.width(16.dp))
                            Button(
                                onClick = {
                                    if (appPackage.isNotEmpty()) {
                                        config.appPackageName = appPackage
                                        showPackageDialog = false
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
    }
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
