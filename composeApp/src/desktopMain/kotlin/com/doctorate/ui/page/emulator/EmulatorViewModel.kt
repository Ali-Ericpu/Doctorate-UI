package com.doctorate.ui.page.emulator

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.doctorate.ui.config.AppConfig
import com.doctorate.ui.util.CommandUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ClassName: EmulatorViewModel
 * Package: com.doctorate.ui.page.emulator
 * Description:
 * @author Raincc
 * @Create 2024/8/14 21:42
 * @Version 1.0
 */
class EmulatorViewModel : ViewModel() {
    private val commandOutput = mutableStateListOf<String>()

    fun commandUpdate(command: String) = commandOutput.add(command)

    fun commandOutput() = commandOutput.toList()

    fun clearCommand() = commandOutput.clear()

    suspend fun connectEmulator(config: AppConfig) = withContext(Dispatchers.IO) {
        val adbToolPath = config.adbToolPath
        CommandUtil.cmdTask(adbToolPath, "connect", config.adbUri)?.also {
            if (it.startsWith("cannot")) {
                throw RuntimeException("Connecting failed, Please check Emulator!")
            }
            commandUpdate(it)
        }
        CommandUtil.cmdTask(adbToolPath, "start-server")
        CommandUtil.cmdTask(adbToolPath, "wait-for-server")
        CommandUtil.cmdTask(adbToolPath, "root")
        CommandUtil.cmdTask(adbToolPath, "reverse", "tcp:${config.serverPort}", "tcp:${config.serverPort}")
        runCatching { CommandUtil.cmdTask("python", "--version") }.onSuccess {
            commandUpdate("Python Version : $it")
        }.onFailure {
            throw RuntimeException("Cannot Find Python, Please Install Python!")
        }
        runCatching { CommandUtil.cmdTask("frida", "--v") }.onSuccess {
            commandUpdate("Frida Version : $it")
        }.onFailure {
            throw RuntimeException("Cannot Find Frida, Please Install Frida!")
        }
        commandUpdate("Running frida-server")
        CommandUtil.cmdAsyncTask(
            adbToolPath,
            "shell",
            "/data/local/tmp/frida-server",
            "&",
            onCommandUpdate = { commandUpdate(it) }
        )
    }

    fun launchEmulator(path: String) = CoroutineScope(Dispatchers.IO).launch {
        CommandUtil.cmdAsyncTask(path, onCommandUpdate = { commandUpdate(it) })
    }

    suspend fun launchGame(config: AppConfig) = withContext(Dispatchers.IO) {
        CommandUtil.cmdAsyncTask(
            "frida",
            "-U",
            "-f",
            config.appPackageName,
            "-l",
            config.scriptPath,
            onCommandUpdate = { commandUpdate(it) }
        )
    }

}