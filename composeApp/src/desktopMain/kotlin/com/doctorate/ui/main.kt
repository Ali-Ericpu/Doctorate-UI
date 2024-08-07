package com.doctorate.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.doctorate.ui.config.AppConfig
import com.doctorate.ui.view.Toast
import doctorateui.composeapp.generated.resources.Res
import doctorateui.composeapp.generated.resources.icon
import org.jetbrains.compose.resources.painterResource
import page.RoutePage

fun main() = application {
    AppWindow()
}

@Composable
@Preview
fun ApplicationScope.AppWindow() {
    var localConfig by remember { mutableStateOf(AppConfig()) }
    Window(
        onCloseRequest = ::exitApplication,
        icon = painterResource(Res.drawable.icon),
        title = "DoctorateUI",
        resizable = false,
        state = WindowState(
            size = DpSize(1280.dp, 800.dp),
            position = WindowPosition(Alignment.Center)
        )
    ) {
        Toast {
            AppConfig(
                onConfigChange = {
                    localConfig = it
                }
            ) {
                MaterialTheme {
                    RoutePage(localConfig)
                }
            }
        }
    }

}