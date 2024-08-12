package com.doctorate.ui.config

import androidx.compose.runtime.*
import com.doctorate.ui.util.JsonUtil
import com.doctorate.ui.view.LocalAppToaster
import kotlinx.coroutines.launch
import java.io.File

data class AppConfig(
    val adbUri: String = "127.0.0.1:7555",
    val serverPort: Int = 8443,
    val appPackageName: String = "",
    val emulatorPath: String = "",
    val customPath: String = "",
    val scriptPath: String = "script/hook.js",
    val adbToolPath: String = "adb/adb.exe",
    val darkMode: Boolean = false,
)

typealias OnConfigChange = (AppConfig) -> Unit

class AppConfigContext(val config: AppConfig = AppConfig(), val onConfigChange: OnConfigChange)

val LocalAppConfig = compositionLocalOf { AppConfigContext { } }

@Composable
fun AppConfig(onConfigChange: OnConfigChange? = null, App: @Composable () -> Unit) {
    var config by remember { mutableStateOf(readConfig()) }
    val coroutineScope = rememberCoroutineScope()
    val toast = LocalAppToaster.current

    remember(config) {
        onConfigChange?.invoke(config)
    }

    CompositionLocalProvider(LocalAppConfig provides AppConfigContext(config) { new ->
        coroutineScope.launch {
            writeConfig(new, onSuccess = {
                toast.toast("success")
            }, onFailure = {
                toast.toastFailure(it)
            })
            config = new
        }
    }) {
        App()
    }
}

fun readConfig(): AppConfig {
    val configFile = File("config/uiConfig.json")
    return if (configFile.exists()) {
        JsonUtil.fromJson(configFile, AppConfig::class.java)
    } else {
        AppConfig().also { JsonUtil.writeJson(it, configFile) }
    }
}

fun writeConfig(newConfig: AppConfig, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    try {
        JsonUtil.writeJson(newConfig, File("config/uiConfig.json"))
        onSuccess()
    } catch (e: Exception) {
        e.printStackTrace()
        onFailure(e.localizedMessage)
    }
}
