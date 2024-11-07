package com.doctorate.ui.entity

import kotlinx.serialization.Serializable

/**
 * ClassName: Result
 * Package: com.doctorate.ui.entity
 * Description:
 * @author Raincc
 * @Create 2024/11/7 13:56
 * @Version 1.0
 */
@Serializable
data class Result<T>(
    val msg: String,
    val status: Int,
    val type: String,
    val data: T? = null
)

