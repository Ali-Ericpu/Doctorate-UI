package com.doctorate.ui.req

import com.doctorate.ui.entity.Item
import kotlinx.serialization.Serializable

/**
 * ClassName: GainItemVO
 * Package: com.doctorate.ui.vo
 * Description:
 * @author Raincc
 * @Create 2024/11/7 14:02
 * @Version 1.0
 */
@Serializable
data class GainItemReq(
    val items: List<Item>
)