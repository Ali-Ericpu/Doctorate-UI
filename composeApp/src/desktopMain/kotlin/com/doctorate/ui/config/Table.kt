package com.doctorate.ui.config

import com.doctorate.ui.util.JsonUtil
import java.io.File

/**
 * ClassName: Table
 * Package: com.doctorate.ui.config
 * Description:
 * @author Raincc
 * @Create 2024/8/16 21:53
 * @Version 1.0
 */
object Table {
    val CHARACTER_TABLE = JsonUtil.transToMap(File("data/excel/character_table.json")) as Map<String, Map<String, Any>>
    val SKIN_TABLE = JsonUtil.transToMap(File("data/excel/skin_table.json"))
    val FAVOR_TABLE = JsonUtil.transToMap(File("data/excel/favor_table.json"))

    fun getCharacterData(charId: String): Map<String, Any> {
        return CHARACTER_TABLE[charId] ?: throw RuntimeException("$charId is not exists")
    }

    fun getSkinPortraitId(skinId: String): String? {
        return JsonUtil.getValue(SKIN_TABLE, "charSkins[\"${skinId}\"].portraitId") as? String
    }

    fun getMaxCharEvoLevel(charId: String): Int {
        return (getCharacterData(charId)["phases"] as List<Any>).size - 1
    }

    fun getMaxCharLevel(charId: String, evoPhase: Int): Int {
        val phases = getCharacterData(charId)["phases"] as List<Map<String, Any>>
        val phase = phases.getOrNull(evoPhase) ?: phases.first()
        return phase["maxLevel"] as Int
    }

    fun getRealFavPoint(percent: Int): Int {
        val favorFrames = FAVOR_TABLE["favorFrames"] as List<Map<String, Any>>
        return favorFrames[percent]["level"] as Int
    }

    fun getFavPointPercent(favPoint: Int): Int {
        val favorFrames = FAVOR_TABLE["favorFrames"] as List<Map<String, Any>>
        favorFrames.forEach {
            if (it["level"] as Int >= favPoint) {
                return JsonUtil.getValue(it, "data.percent") as Int
            }
        }
        return 0
    }
}