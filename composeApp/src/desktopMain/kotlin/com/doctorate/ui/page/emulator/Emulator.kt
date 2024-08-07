package com.doctorate.ui.page.emulator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.doctorate.ui.Greeting
import com.doctorate.ui.config.AppConfig
import com.doctorate.ui.view.Toast
import doctorateui.composeapp.generated.resources.Res
import doctorateui.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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
fun Emulator(adbConfig: AppConfig) {
    var adbHost by remember { mutableStateOf(adbConfig.adbHost) }
    var adbPort by remember { mutableStateOf(adbConfig.adbPort.toString()) }
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
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
            Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                Text("保存并连接")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
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
    Spacer(modifier = Modifier.height(40.dp))
    OutlinedTextField(
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        value = (adbHost),
        onValueChange = { onHostChange(it) },
        placeholder = {
            Text(
                "Enter ADB Host",
                color = MaterialTheme.colors.onPrimary
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            unfocusedBorderColor = MaterialTheme.colors.onSurface,
            focusedBorderColor = MaterialTheme.colors.onSurface,
        )
    )
    Spacer(modifier = Modifier.height(20.dp))
    OutlinedTextField(
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        value = (adbPort.toString()),
        onValueChange = { onPortChange(it) },
        placeholder = {
            Text(
                "Enter ADB Host", style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onPrimary
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = MaterialTheme.colors.surface,
            unfocusedBorderColor = MaterialTheme.colors.onSurface,
            focusedBorderColor = MaterialTheme.colors.onSurface,
        )
    )
}

@Composable
fun SaveAndConnectEmulator(adbConfig: AppConfig) {
    val (host, port) = adbConfig
//    val create = Dadb.create(host, port)
    Toast { "adb shell getprop $host:$port" }
    Text("$host:$port")
}