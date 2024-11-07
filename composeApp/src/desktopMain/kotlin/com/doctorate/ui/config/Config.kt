package com.doctorate.ui.config

import androidx.compose.runtime.*
import com.doctorate.ui.util.JsonUtil
import com.doctorate.ui.util.log
import com.doctorate.ui.view.LocalAppToaster
import kotlinx.coroutines.launch
import java.io.File

data class AppConfig(
    val adbUri: String = "127.0.0.1:7555",
    val serverUri: String = "http://127.0.0.1",
    val serverPort: String = "8443",
    val appPackageName: String = "",
    val emulatorPath: String = "",
    val customPath: String = "",
    val scriptPath: String = "script/hook.js",
    val adbToolPath: String = "adb/adb.exe",
    val darkMode: Boolean = false,
    val extraMode: Boolean = false,
    val uid: String = "",
    val adminKey: String = "",
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
    if (GlobalConfig.config != null) return GlobalConfig.config!!
    val configFile = File("config/uiConfig.json")
    return if (configFile.exists()) {
        JsonUtil.fromJson<AppConfig>(configFile)
    } else {
        AppConfig().also { writeConfig(it, onSuccess = {}, onFailure = {}) }
    }
}

fun writeConfig(newConfig: AppConfig, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    try {
        GlobalConfig.config = newConfig
        JsonUtil.writeToFile(newConfig, File("config/uiConfig.json"))
        onSuccess()
    } catch (e: Exception) {
        e.printStackTrace()
        onFailure(e.localizedMessage)
    }
}

object GlobalConfig {
    var config: AppConfig? = null
        get() = field.also { log().info("Load Config") }

    init {
        config = readConfig()
    }
}
