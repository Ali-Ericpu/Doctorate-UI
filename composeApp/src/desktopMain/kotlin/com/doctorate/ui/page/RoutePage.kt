package page

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.doctorate.ui.App
import com.doctorate.ui.config.AppConfig
import com.doctorate.ui.page.emulator.Emulator


private enum class PageRoute(val desc: String) {
    EMULATOR("模拟器"), FRIDA("脚本"), SETTING("设置")
}

@Composable
@Preview
fun RoutePage(appConfig: AppConfig) {
    var selection by remember {
        mutableStateOf(PageRoute.entries.first())
    }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxHeight().weight(1f).background(Color(245, 245, 245))
        ) {
            PageRoute.entries.map {
                ActionTab(text = it.desc, selected = it == selection) {
                    selection = it
                }
            }
        }

        Box(modifier = Modifier.fillMaxHeight().weight(4f).padding(20.dp)) {
            when(selection) {
                PageRoute.EMULATOR -> {
                    Emulator(
                        appConfig
                    )
                }
                PageRoute.SETTING -> {
                    App()
                }
                PageRoute.FRIDA -> {
                }
                else -> {
                    Text(selection.name, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun ActionTab(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .background(if (!selected) Color(245, 245, 245) else Color.White)
        .clickable {
        onClick()
    }) {
        Text(text, modifier = Modifier.align(Alignment.Center))
    }
}