package com.doctorate.ui.entity

import kotlinx.serialization.Serializable

/**
 * ClassName: Item
 * Package: com.doctorate.ui.entity
 * Description:
 * @author Raincc
 * @Create 2024/11/7 14:02
 * @Version 1.0
 */
@Serializable
data class Item(
    val id: String,
    val type: String,
    val count: Int
)