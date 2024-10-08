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
import com.doctorate.ui.config.readConfig
import com.doctorate.ui.page.RoutePage
import com.doctorate.ui.theme.darkTheme
import com.doctorate.ui.theme.lightTheme
import com.doctorate.ui.view.Toast
import doctorateui.composeapp.generated.resources.Res
import doctorateui.composeapp.generated.resources.compose_multiplatform
import doctorateui.composeapp.generated.resources.title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    AppWindow()
}

@Composable
@Preview
fun ApplicationScope.AppWindow() {
    var localConfig by remember { mutableStateOf(readConfig()) }
    Window(
        onCloseRequest = ::exitApplication,
        icon = painterResource(Res.drawable.compose_multiplatform),
        title = stringResource(Res.string.title),
        resizable = true,
        state = WindowState(
            size = DpSize(1100.dp, 680.dp),
            position = WindowPosition(Alignment.Center)
        )
    ) {
        Toast {
            AppConfig(
                onConfigChange = {
                    localConfig = it
                }
            ) {
                MaterialTheme(if (localConfig.darkMode) darkTheme else lightTheme) {
                    RoutePage()
                }
            }
        }
    }

}