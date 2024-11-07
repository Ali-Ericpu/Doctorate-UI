package com.doctorate.ui.req

import kotlinx.serialization.Serializable

/**
 * ClassName: UnlockAllCharReq
 * Package: com.doctorate.ui.req
 * Description:
 * @author Raincc
 * @Create 2024/11/7 23:00
 * @Version 1.0
 */
@Serializable
data class UnlockAllCharReq(
    val favorPoint: Int,
    val potentialRank: Int,
    val specializeLevel: Int,
    val mainSkillLvl: Int,
    val evolvePhase: Int,
    val level: Int,
    val equipLevel: Int,
    val enableRogueChar: Boolean
)