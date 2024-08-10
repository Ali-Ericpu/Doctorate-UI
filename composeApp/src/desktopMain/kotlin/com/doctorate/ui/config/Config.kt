package com.doctorate.ui.config

import androidx.compose.runtime.*
import com.doctorate.ui.view.LocalAppToaster
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileReader

data class AppConfig(
    val adbUri: String = "127.0.0.1:7555",
    var appPackageName: String = "",
    var emulatorPath: String = "",
    val scriptPath: String = "script/hook.js",
)

typealias OnConfigChange = (AppConfig) -> Unit

class AppConfigContext(val config: AppConfig = AppConfig(), val onConfigChange: OnConfigChange)

val gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()
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
    return try {
        val configFile = File("config.json")
        gson.fromJson(FileReader(configFile), AppConfig::class.java)
    } catch (_: Exception) {
        AppConfig()
    }
}

fun writeConfig(newConfig: AppConfig, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    try {
        val configFile = File("config.json")
        configFile.outputStream().write(gson.toJson(newConfig).toByteArray())
        onSuccess()
    } catch (e: Exception) {
        e.printStackTrace()
        onFailure(e.localizedMessage)
    }
}
