package com.doctorate.ui.page.emulator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import androidx.lifecycle.viewmodel.compose.viewModel
import com.doctorate.ui.config.AppConfig
import com.doctorate.ui.config.LocalAppConfig
import com.doctorate.ui.config.readConfig
import com.doctorate.ui.page.character.CircleIconButton
import com.doctorate.ui.util.log
import com.doctorate.ui.view.FileDialog
import com.doctorate.ui.view.LocalAppToaster
import doctorateui.composeapp.generated.resources.Res
import doctorateui.composeapp.generated.resources.adb_uri
import doctorateui.composeapp.generated.resources.connect_emulator
import doctorateui.composeapp.generated.resources.custom
import doctorateui.composeapp.generated.resources.launch_game
import doctorateui.composeapp.generated.resources.open_emulator
import doctorateui.composeapp.generated.resources.save
import doctorateui.composeapp.generated.resources.select_script
import doctorateui.composeapp.generated.resources.type_package
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.io.File

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
fun Emulator(
    viewModel: EmulatorViewModel = viewModel { EmulatorViewModel() }
) {
    val toast = LocalAppToaster.current
    val config = LocalAppConfig.current.config
    val onConfigChange = LocalAppConfig.current.onConfigChange
    val commandOutput = viewModel.commandOutput()
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    var adbUri by rememberSaveable { mutableStateOf(config.adbUri) }
    Surface(color = MaterialTheme.colors.background) {
        Column(Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)).background(color = Color.LightGray)) {
            ConnectEmulatorButton(
                adbUri = adbUri,
                connectEmulator = {
                    coroutineScope.launch {
                        try {
                            viewModel.connectEmulator(readConfig())
                        } catch (e: Exception) {
                            viewModel.commandUpdate(e.message.toString().also { toast.toastFailure(it) })
                        }
                    }
                },
                onAdbUriChange = { adbUri = it },
                onAdbUriSave = { onConfigChange(it) }
            )
            CustomButtons(
                config = config,
                launchEmulator = { viewModel.launchEmulator(it) },
                launchGame = {
                    coroutineScope.launch {
                        try {
                            viewModel.launchGame(readConfig())
                        } catch (e: Exception) {
                            viewModel.commandUpdate(e.message.toString().also { toast.toastFailure(it) })
                        }
                    }
                },
                onCommandUpdate = { viewModel.commandUpdate(it) },
                onConfigChange = onConfigChange
            )
            Box {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(color = Color.Black)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                    state = lazyListState,
                ) {
                    items(commandOutput) { output ->
                        Text(text = output, color = Color.LightGray, fontWeight = FontWeight.Bold)
                        if (commandOutput.size > 10) {
                            coroutineScope.launch {
                                lazyListState.animateScrollToItem(commandOutput.lastIndex)
                            }
                        }
                    }
                    item { Text("->", color = MaterialTheme.colors.primary) }
                }
                CircleIconButton(
                    icon = Icons.Default.Delete,
                    onclick = { viewModel.clearCommand() },
                    size = 56,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ConnectEmulatorButton(
    adbUri: String,
    connectEmulator: () -> Unit,
    onAdbUriChange: (String) -> Unit,
    onAdbUriSave: (AppConfig) -> Unit,
) {
    val toast = LocalAppToaster.current
    val regex =
        Regex("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]):(?:[0-9]|[1-9][0-9]{1,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])\$")
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
                Text(
                    text = stringResource(Res.string.adb_uri),
                    color = Color.Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
            },
            singleLine = true,
            isError = !regex.matches(adbUri),
            value = adbUri,
            onValueChange = { onAdbUriChange(it) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.LightGray,
                unfocusedBorderColor = MaterialTheme.colors.onSurface,
                focusedBorderColor = Color.Black,
            ),
            modifier = Modifier.weight(7f)
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.weight(3f)
        ) {
            val modifier = Modifier.fillMaxHeight().width(100.dp)
            Button(
                modifier = modifier,
                enabled = regex.matches(adbUri),
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (!regex.matches(adbUri)) {
                            throw RuntimeException("Illegal adb uri").apply { toast.toastFailure(message!!) }
                        }
                        onAdbUriSave(readConfig().copy(adbUri = adbUri))
                    }
                }) {
                Text(stringResource(Res.string.save))
            }
            Button(
                modifier = modifier,
                enabled = regex.matches(adbUri),
                onClick = connectEmulator
            ) {
                Text(stringResource(Res.string.connect_emulator))
            }
        }
    }
}

@Composable
fun CustomButtons(
    config: AppConfig,
    launchEmulator: (String) -> Unit,
    launchGame: () -> Unit,
    onCommandUpdate: (String) -> Unit,
    onConfigChange: (AppConfig) -> Unit,
) {
    var showEmulatorDialog by remember { mutableStateOf(false) }
    var showScriptDialog by remember { mutableStateOf(false) }
    var showPackageDialog by remember { mutableStateOf(false) }
    var showCustomDialog by remember { mutableStateOf(false) }
    var scriptState by rememberSaveable { mutableStateOf(File(config.scriptPath).exists()) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp).height(56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { showEmulatorDialog = true }, modifier = Modifier.size(100.dp),
        ) {
            Text(stringResource(Res.string.open_emulator))
            if (showEmulatorDialog) {
                if (config.emulatorPath.isNotEmpty()) {
                    showEmulatorDialog = false
                    launchEmulator(config.emulatorPath)
                } else {
                    FileDialog(
                        onCloseRequest = {
                            showEmulatorDialog = false
                            it?.also {
                                if (it.endsWith(".exe")) {
                                    launchEmulator(
                                        it.also { onConfigChange(config.copy(emulatorPath = it)) },
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
            Text(text = stringResource(Res.string.select_script), modifier = Modifier.align(Alignment.CenterVertically))
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
            Text(stringResource(Res.string.launch_game))
            if (showPackageDialog) {
                if (config.appPackageName.isNotEmpty()) {
                    showPackageDialog = false
                    launchGame()
                } else {
                    EnterTextDialog(
                        title = stringResource(Res.string.type_package),
                        onSave = {
                            showPackageDialog = false
                            it?.let {
                                config.copy(appPackageName = it).also { onConfigChange(it) }
                                launchGame()
                            }
                        }
                    )
                }
            }
        }
        Button(
            onClick = { showCustomDialog = true },
            modifier = Modifier.size(100.dp)
        ) {
            Text(stringResource(Res.string.custom))
            if (showCustomDialog) {
                if (config.customPath.isNotEmpty()) {
                    showCustomDialog = false
                    launchEmulator(config.customPath)
                } else {
                    FileDialog(
                        onCloseRequest = {
                            showCustomDialog = false
                            log().info(it)
                            it?.also {
                                onConfigChange(config.copy(customPath = it.also { launchEmulator(it) }))
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EnterTextDialog(
    title: String,
    onSave: (String?) -> Unit,
) {
    var value by remember { mutableStateOf("") }
    DialogWindow(
        onCloseRequest = { onSave(null) },
        resizable = false,
        title = title,
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
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.width(280.dp).fillMaxHeight()
            )
            Spacer(Modifier.width(16.dp))
            Button(
                enabled = value.isNotEmpty(),
                onClick = { onSave(value) },
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(stringResource(Res.string.save))
            }
        }
    }
}




