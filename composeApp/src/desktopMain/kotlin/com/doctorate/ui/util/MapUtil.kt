package com.doctorate.ui.util

import ognl.Ognl

/**
 * ClassName: MapUtil
 * Package: com.doctorate.ui.util
 * Description:
 * @author Raincc
 * @Create 2024/11/7 15:39
 * @Version 1.0
 */

inline fun <reified T> Map<String, Any>.get(key: String) = get(key) as? T

inline fun <reified T> Map<String, Any>.getValue(path: String) = Ognl.getValue(path, this) as? T
