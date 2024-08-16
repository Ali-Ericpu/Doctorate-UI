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
    val CHARACTER_TABLE: Map<String, Map<String, Any>> =
        JsonUtil.transToMap(File("data/excel/character_table.json")) as Map<String, Map<String, Any>>
    val GAME_DATA_TABLE: Map<String, Any> = JsonUtil.transToMap(File("data/excel/gamedata_const.json"))
    val SKIN_TABLE: Map<String, Any> = JsonUtil.transToMap(File("data/excel/skin_table.json"))
    val FAVOR_TABLE: Map<String, Any> = JsonUtil.transToMap(File("data/excel/favor_table.json"))

    fun getCharacterData(charId: String): Map<String, Any> {
        return CHARACTER_TABLE[charId] ?: throw RuntimeException("char data not found $charId")
    }

    fun getSkinPortraitId(skinId: String): String {
        return JsonUtil.getValue(SKIN_TABLE, "charSkins[\"${skinId}\"].portraitId") as String
    }

    fun getMaxCharEvoLevel(charId: String): Int {
        return (getCharacterData(charId)["phases"] as List<Any>).size - 1
    }

    fun getMaxCharLevel(charId: String, evoPhase: Int): Int {
        val phases = getCharacterData(charId)["phases"] as List<Map<String, Any>>
        val phase = phases.getOrNull(evoPhase) ?: phases.first()
        return (phase["maxLevel"] as Double).toInt()
    }

    fun getRealFavPoint(percent: Int): Int {
        val favorFrames = FAVOR_TABLE["favorFrames"] as List<Map<String, Any>>
        return (favorFrames[percent]["level"] as Double).toInt()
    }

    fun getFavPointPercent(favPoint: Int): Int {
        val favorFrames = FAVOR_TABLE["favorFrames"] as List<Map<String, Any>>
        favorFrames.forEach {
            if (it["level"] as Double >= favPoint) {
                return (JsonUtil.getValue(it, "data.percent") as Double).toInt()
            }
        }
        return 0
    }
}