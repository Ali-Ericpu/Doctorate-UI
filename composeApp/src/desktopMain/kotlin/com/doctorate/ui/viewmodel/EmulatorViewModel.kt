package com.doctorate.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

/**
 * ClassName: EmulatorViewModel
 * Package: com.doctorate.ui.viewmodel
 * Description:
 * @author Raincc
 * @Create 2024/8/10 16:20
 * @Version 1.0
 */
class EmulatorViewModel: ViewModel() {

    private var _commandOutput = mutableListOf<String>()
    val commandOutput: List<String> = _commandOutput
    fun addCommand(command: String) {
        mutableStateListOf<String>()
    }
}