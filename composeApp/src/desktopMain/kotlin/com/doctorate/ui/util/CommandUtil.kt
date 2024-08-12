package com.doctorate.ui.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * ClassName: CommandUtil
 * Package: com.doctorate.ui
 * Description:
 * @author Raincc
 * @Create 2024/8/8 15:22
 * @Version 1.0
 */
object CommandUtil {

    fun cmdTask(vararg command: String): String? {
        val process = ProcessBuilder().command(*command).start()
        val readLine = process.inputReader().readLine()
        process.waitFor(1, TimeUnit.SECONDS)
        return readLine
    }

    suspend fun cmdAsyncTask(vararg command: String, onCommandUpdate: (String) -> Unit) = withContext(Dispatchers.IO) {
        val process = ProcessBuilder().command(*command).start()
        process.inputReader().use {
            try {
                while (true) {
                    val line = it.readLine() ?: break
                    onCommandUpdate(line)
                }
            } catch (e: Exception) {
                onCommandUpdate(e.message.toString())
            }
        }
    }
}