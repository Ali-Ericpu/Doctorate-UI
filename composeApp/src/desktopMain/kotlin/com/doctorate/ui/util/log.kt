package com.doctorate.ui.util

import org.slf4j.LoggerFactory

/**
 * ClassName: log
 * Package: com.doctorate.ui.util
 * Description:
 * @author Raincc
 * @Create 2024/11/7 15:18
 * @Version 1.0
 */

fun <T: Any> T.log() = LoggerFactory.getLogger(this.javaClass)