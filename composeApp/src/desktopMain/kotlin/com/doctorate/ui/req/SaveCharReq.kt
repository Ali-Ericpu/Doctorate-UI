package com.doctorate.ui.req

import com.doctorate.ui.entity.Char
import kotlinx.serialization.Serializable

/**
 * ClassName: SaveCharVO
 * Package: com.doctorate.ui.vo
 * Description:
 * @author Raincc
 * @Create 2024/11/7 14:01
 * @Version 1.0
 */
@Serializable
data class SaveCharReq(
    val charInstId: Int,
    val char: Char
)