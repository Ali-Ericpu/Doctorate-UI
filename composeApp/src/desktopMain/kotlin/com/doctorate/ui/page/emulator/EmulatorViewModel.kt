package com.doctorate.ui.page.emulator

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

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

    fun commandUpdate(command: String) {
        commandOutput.add(command)
    }

    fun commandOutput() = commandOutput.toList()

    fun clearCommand() = commandOutput.clear()

}